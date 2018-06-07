package noelanthony.com.lostandfoundfinal.newsfeed;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import noelanthony.com.lostandfoundfinal.ChatMessagesActivity;
import noelanthony.com.lostandfoundfinal.R;
import noelanthony.com.lostandfoundfinal.maps.MapsActivity;
import noelanthony.com.lostandfoundfinal.messegesFragment;
import noelanthony.com.lostandfoundfinal.navmenu.newsFeedActivity;

public class onItemClickActivity extends AppCompatActivity{

    private ImageView itemImageView;
    private TextView lostorfoundStatusTextView,itemNameTextView,dateandtimeTextView,locationTextView,descriptionTextView,posterTextView,clicktomessageTextView,setToFoundTextView;
    private String userID,uid,poster;//,longitude,latitude;
    private ImageButton googleMapImageButton;
    private LinearLayout foundOnlyFrame;
    private Double longitude,latitude;
    private LinearLayout header;


    //FOR MYSUBMISSIONS
    private FirebaseAuth mAuth;
    private DatabaseReference dbReference,mDatabase;

    //FIREBASE STUFF
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_item_click);

        String itemName = "";
        String lostOrFoundStatus= "";
        String date = "";
        String location = "";
        String description = "";
        poster = "";
        String imageId = "";
        uid = "";
        String visibility ="";
         latitude=null;
         longitude=null;
        Intent intent = getIntent();
        if (null!= intent){
            itemName = intent.getStringExtra("item_name");
            lostOrFoundStatus = intent.getStringExtra("item_status");
            date = intent.getStringExtra("item_date_time");
            location = intent.getStringExtra("item_location");
            description = intent.getStringExtra("item_description");
            poster = intent.getStringExtra("item_poster");
            imageId = intent.getStringExtra("item_image_id");
            uid = intent.getStringExtra("item_uid");
            visibility = intent.getStringExtra("visibility");
            latitude = intent.getDoubleExtra("item_latitude",0.000);
            longitude = intent.getDoubleExtra("item_longitude",0.000);
        }


            //itemposition = getIntent().getExtras().getInt("position");
        itemImageView = findViewById(R.id.itemImageView);
        lostorfoundStatusTextView = findViewById(R.id.lostorfoundStatusTextView);
        itemNameTextView = findViewById(R.id.itemNameTextView);
        dateandtimeTextView = findViewById(R.id.dateandtimeTextView);
        locationTextView = findViewById(R.id.locationTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        posterTextView = findViewById(R.id.posterTextView);
        clicktomessageTextView = findViewById(R.id.clicktomessageTextView);
        setToFoundTextView = findViewById(R.id.setToFoundTextView);
        googleMapImageButton = findViewById(R.id.googleMapImageButton);
        foundOnlyFrame = findViewById(R.id.foundOnlyFrame);
        header= findViewById(R.id.header);



        if (lostOrFoundStatus.equals("Found") && latitude!=0.0 && longitude!=0.0) {
            foundOnlyFrame.setVisibility(View.VISIBLE);
            googleMapImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent startIntent = new Intent(onItemClickActivity.this,MapsActivity.class);
                    startIntent.putExtra("item_longitudeMap", longitude);
                    startIntent.putExtra("item_latitudeMap", latitude);
                    startActivity(startIntent);

                    //String lat = Double.toString(latitude);
                    //String longi = Double.toString(longitude);
                    //Toast.makeText(onItemClickActivity.this, lat + longi, Toast.LENGTH_LONG).show();
                }
            });
        }
        //setToFoundTextView is visible is item is lost,  clicktomessageTextView is invisible if launched from mySubmissions
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        //if isVisible!=null then it is from my submissions page
        //NEWS FEED ONCLICK
        if(visibility.equals("newsLost")) {
            clicktomessageTextView.setText("Were you able to find this item? Click to contact the poster");
            clicktomessageTextView.setVisibility(View.VISIBLE);
            setToFoundTextView.setVisibility(View.INVISIBLE);
        } else if (visibility.equals("newsFound")){
            clicktomessageTextView.setText("Do you own this item? Click to message poster");
            clicktomessageTextView.setVisibility(View.VISIBLE);
            setToFoundTextView.setVisibility(View.INVISIBLE);
        }else if (visibility.equals("myLost")){
            setToFoundTextView.setVisibility(View.VISIBLE);
            clicktomessageTextView.setVisibility(View.INVISIBLE);
        }else if(visibility.equals("myFound")){
            setToFoundTextView.setText("Is this item already claimed by the owner? Set to Claimed");
            setToFoundTextView.setVisibility(View.VISIBLE);
            clicktomessageTextView.setVisibility(View.INVISIBLE);
        }
        clicktomessageTextView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent  =  new Intent(onItemClickActivity.this,ChatMessagesActivity.class
                );
                intent.putExtra("item_poster", poster);
                intent.putExtra("item_uid", uid);
                //Toast.makeText(onItemClickActivity.this, uid, Toast.LENGTH_SHORT).show();
                startActivity(intent);
                /*Bundle bundle = new Bundle();
                bundle.putString( "item_uid",uid);
                bundle.putString("item_poster",poster );
                messegesFragment myObj = new messegesFragment();
                myObj.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();

                fragmentManager.beginTransaction().replace(R.id.content_frame, new messegesFragment()).commit();*/
            }
            });
        RequestOptions options = new RequestOptions();
        options.fitCenter();
        if(imageId==null){
            Glide.with(onItemClickActivity.this).load(R.mipmap.ic_noimage).apply(options).into(itemImageView);
        }else {
            Glide.with(onItemClickActivity.this).load(imageId).into(itemImageView); // IMAGE VIEW
        }
        //IF ITEM IS LOST, SET COLOR TO RED
        if(lostOrFoundStatus.equals("Lost")){
            header.setBackgroundColor(getApplication().getResources().getColor(R.color.lostItemColor));
            lostorfoundStatusTextView.setTextColor(getApplication().getResources().getColor(R.color.lostItemColor));
            itemNameTextView.setTextColor(getApplication().getResources().getColor(R.color.colorPrimaryDark));
            dateandtimeTextView.setTextColor(getApplication().getResources().getColor(R.color.lostItemColor));
            locationTextView.setTextColor(getApplication().getResources().getColor(R.color.lostItemColor));
            descriptionTextView.setTextColor(getApplication().getResources().getColor(R.color.colorPrimaryDark));
            posterTextView.setTextColor(getApplication().getResources().getColor(R.color.colorPrimaryDark));
            //clicktomessageTextView.setTextColor(getApplication().getResources().getColor(R.color.lostItemColor));
            //setToFoundTextView.setTextColor(getApplication().getResources().getColor(R.color.lostItemColor));

        }

        itemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageId2 = "";
                Intent intentFromClick = getIntent();
                if (null!= intentFromClick) {
                    imageId2 = intentFromClick.getStringExtra("item_image_id");
                }
                //startActivity(new Intent(onItemClickActivity.this, PopupImage.class));
                Intent intent = new Intent(onItemClickActivity.this,PopupImage.class);
                intent.putExtra("image_id",imageId2);
                startActivity(intent);
            }
        });
          /*
        posterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String posterName;
                String posterUid;
                Intent intentFromClick = getIntent();
                if (null!= intentFromClick) {
                    //posterName= intentFromClick.getStringExtra("item_poster");
                    posterUid = intentFromClick.getStringExtra("item_uid");
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://lostandfoundfinal.firebaseio.com/");
                    DatabaseReference profRef = dbRef.child("users").child(posterUid);
                    profRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserInformation uInfo = new UserInformation();
                            uInfo.setName(dataSnapshot.getValue(UserInformation.class).getName());//sets name
                            uInfo.setImage(dataSnapshot.getValue(UserInformation.class).getImage());//sets name
                            Intent intent = new Intent(onItemClickActivity.this,PopupProfile.class);
                            intent.putExtra("poster_image",uInfo.getImage());
                            intent.putExtra("poster_name", uInfo.getName());
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        */

        lostorfoundStatusTextView.setText(lostOrFoundStatus);
        itemNameTextView.setText(itemName);
        dateandtimeTextView.setText(date);
        locationTextView.setText(location);
        descriptionTextView.setText(description);
        posterTextView.setText("Posted by " + poster);

        if(lostOrFoundStatus.equals("Lost") && uid.equals(userID)){
            clicktomessageTextView.setVisibility(View.INVISIBLE);
            setToFoundTextView.setVisibility(View.VISIBLE);
        } else if (lostOrFoundStatus.equals("Found") && uid.equals(userID)){
            setToFoundTextView.setText("Is this item already claimed by owner? Set to Claimed");
            setToFoundTextView.setVisibility(View.VISIBLE);
            clicktomessageTextView.setVisibility(View.INVISIBLE);
        }

    }//ONCREATE END
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.setToFoundTextView:
                showAlertDialog(v);
                break;
        }
    }

    private void showAlertDialog(View v) {
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://lostandfoundfinal.firebaseio.com/");
        dbReference= mDatabase.child("items");
        final String itemId;
        Intent intent = getIntent();
        itemId = intent.getStringExtra("item_id");
        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setCancelable(true);
        alert.setTitle("Set item from lost to found");
        alert.setMessage("Confirm " + itemNameTextView.getText() + " is now claimed by the owner");
        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //MOVE A CHILD TO A NEW NODE
                DatabaseReference fromPath = dbReference.child(itemId); //WHICH CHILD TO COPY FROM
                DatabaseReference toPath = mDatabase.child("ClaimedItems"); //WHICH CHILD IT GOES
                String key = fromPath.getKey().toString();
                moveFirebaseRecord(fromPath,toPath,key);

                Toast.makeText(onItemClickActivity.this, "Lost "+ itemNameTextView.getText() +" Set to claimed", Toast.LENGTH_SHORT).show();
                Intent startIntent = new Intent(onItemClickActivity.this,newsFeedActivity.class);
                startActivity(startIntent);

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.create().show();
    }

    public void moveFirebaseRecord(final DatabaseReference fromPath, final DatabaseReference toPath, final String key) {
        fromPath.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                toPath.child(dataSnapshot.getKey())
                        .setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {

                            System.out.println("Copy failed");

                        } else {
                            System.out.println("Success");
                            fromPath.setValue(null);
                            toPath.child(key).child("Owner").setValue(userID); //THIS SETS THE FOUNDER TO USER SELF
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Copy failed");
            }
        });
    }

}//END OF ACTIVITY
