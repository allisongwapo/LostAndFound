package noelanthony.com.lostandfoundfinal.newsfeed;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import noelanthony.com.lostandfoundfinal.R;
import noelanthony.com.lostandfoundfinal.loginregister.MainActivity;
import noelanthony.com.lostandfoundfinal.maps.LocationTrack;
import noelanthony.com.lostandfoundfinal.navmenu.newsFeedActivity;
import noelanthony.com.lostandfoundfinal.profile.UserInformation;

public class submitFoundItemActivity extends AppCompatActivity {

    private EditText itemnameEditText;
    private AutoCompleteTextView locationdescEditText;
    private EditText descriptionEditText;
    private ImageButton uploadImageButton,googleMapImageButton;//,cameraImageButton;
    private Button submitFoundButton, cancelLocationBtn;
    private ProgressBar progressBar;
    private DatabaseReference mDatabase, nameRef;
    private FirebaseAuth mAuth;
    private String userID, poster, locationSelection;
    private Double longitude,latitude;
    private ProgressBar mProgressBar;
    private ImageView displayImageView;
    Context applicationContext = MainActivity.getContextOfApplication();
    //for image storage
    private StorageReference mStorage;
    private static final int PICK_IMAGE_REQUEST = 1;
    //private static final int CAMERA_REQUEST = 1888;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private TextView currentLocationTextView;

    //notifications

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_found_item);
        FirebaseMessaging.getInstance().subscribeToTopic("matcher");


        itemnameEditText = findViewById(R.id.itemnameEditText);
        locationdescEditText = findViewById(R.id.locationdescEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        submitFoundButton = findViewById(R.id.submitFoundBtn);
        mProgressBar = findViewById(R.id.progressbar);
        displayImageView = findViewById(R.id.displayImageView);
        googleMapImageButton  = findViewById(R.id.googleMapImageButton);
        currentLocationTextView = findViewById(R.id.currentLocationTextView);
        cancelLocationBtn = findViewById(R.id.cancelLocationBtn);
        //cameraImageButton = findViewById(R.id.cameraImageButton);
        String[] talamban_buildings = getResources().getStringArray(R.array.talamban_buildings_array);
        ArrayAdapter<String> autoCompleteAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, talamban_buildings);
        locationdescEditText.setAdapter(autoCompleteAdapter);

        mStorageRef = FirebaseStorage.getInstance().getReference("lostItemImage/");
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        locationSelection="None";
        googleMapImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent startIntent = new Intent(submitFoundItemActivity.this,MapsActivity.class);
                //startActivity(startIntent);
               final  LocationTrack locationTrack = new LocationTrack(submitFoundItemActivity.this);
                if (locationTrack.canGetLocation()) {
                            longitude = locationTrack.getLongitude();
                            latitude = locationTrack.getLatitude();
                            locationSelection= "Current Location";
                            currentLocationTextView.setText("Current Location");
                            Toast.makeText(submitFoundItemActivity.this, "Item Location set to current location", Toast.LENGTH_SHORT).show();
                    cancelLocationBtn.setVisibility(View.VISIBLE );
                    cancelLocationBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            locationSelection="None";
                            longitude = null;
                            latitude = null;
                            Toast.makeText(submitFoundItemActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                            cancelLocationBtn.setVisibility(View.GONE);
                            currentLocationTextView.setText("(Click to get current location)");
                        }
                    });

                } else {
                    locationTrack.showSettingsAlert();
                }
            }
        });

        //UPLOAD IMAGE
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

       /* cameraImageButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cameraIntent();
        }
    });*/

        submitFoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(v);
            }
        });

    }


    public void showAlertDialog(View v){
        final String itemName = itemnameEditText.getText().toString().trim();
        final String locationDescription = locationdescEditText.getText().toString().trim();
        final String description = descriptionEditText.getText().toString().trim();
        final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        //check if name is empty
        if(itemName.isEmpty()){
            itemnameEditText.setError("Item name is required");
            itemnameEditText.requestFocus();
            return;
        }

        if(description.isEmpty()){
            descriptionEditText.setError("Description is required");
            descriptionEditText.requestFocus();
            return;
        }
        if(locationDescription.isEmpty()){
            locationdescEditText.setError("Location Description is required");
            locationdescEditText.requestFocus();
            return;
        }
        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setCancelable(true);
        alert.setTitle("Confirm");
        alert.setMessage("Item Name: " + itemnameEditText.getText() + "\n Item Description: " + descriptionEditText.getText()+ "\n Location Description: "
                        + locationdescEditText.getText() +"\n Location: " + locationSelection);
        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("items");
                DatabaseReference nameRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID);//to get the poster name
                DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference().child("admin").child("Ui2KIyn7socV7MnPrmp6YCnH1xI2").child("notifications");//to send admin notification

                final DatabaseReference item = mDatabase.push();
                String status = "Found";
                String key = item.getKey();
                item.child("itemName").setValue(itemName);
                //ADDITIONS FOR MATCHING ALGO
               // matcherItem.child("itemName").setValue(itemName);
                //matcherItem.child("description").setValue(description);
                matcher(itemName,description,key,status);
                //END MATCHER
                item.child("locationDescription").setValue(locationDescription);

                if(longitude != null && latitude != null ) {
                   // item.child("location").child("longitude").setValue(longitude);
                    //item.child("location").child("latitude").setValue(latitude);
                    item.child("longitude").setValue(longitude);
                    item.child("latitude").setValue(latitude);
                }
                item.child("description").setValue(description);
                nameRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        showData(dataSnapshot);
                        item.child("poster").setValue(poster);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                item.child("dateSubmitted").setValue(currentDateTimeString);
                item.child("status").setValue("Found");
                item.child("uid").setValue(userID); //for mySubmissions Filter
                item.child("approvalStatus").setValue(0);
                item.child("itemID").setValue(key);
                //this block of code prevents multiple image upload
                if(mUploadTask!=null && mUploadTask.isInProgress()){
                    Toast.makeText(submitFoundItemActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }
                else if(mImageUri!=null){
                    StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));
                    mUploadTask = fileReference.putFile(mImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    mProgressBar.setVisibility(View.VISIBLE);
                                    Handler handler = new Handler(); //to delay progress bar by half a second
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mProgressBar.setProgress(0);
                                        }
                                    },500);
                                    Toast.makeText(submitFoundItemActivity.this,"Upload successful", Toast.LENGTH_LONG).show();
                                    // items upload = new items (itemnameEditText.getText().toString().trim(),taskSnapshot.getDownloadUrl().toString());
                                    // String uploadId = mDatabaseRef.push().getKey();
                                    item.child("imageID").setValue(taskSnapshot.getDownloadUrl().toString());

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(submitFoundItemActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    mProgressBar.setProgress((int)progress);
                                }
                            });
                }else{
                    Toast.makeText(submitFoundItemActivity.this,"No file selected", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), "Submission Successful and awaiting admin approval", Toast.LENGTH_SHORT).show();
                Intent startIntent = new Intent(getApplicationContext(),newsFeedActivity.class);
                startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(startIntent);

                //FOR NOTIFICATION made on 05/15/2018
                Map<String,Object> notificationMessage = new HashMap<>();
                notificationMessage.put("message","found " + itemName);
                notificationMessage.put("from", userID);
                DatabaseReference notification = adminRef.push();
                notification.setValue(notificationMessage);


            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.create().show();
    }
    private void showData(DataSnapshot dataSnapshot) {
        UserInformation uInfo = new UserInformation();
        uInfo.setName(dataSnapshot.getValue(UserInformation.class).getName());//sets name
        poster = uInfo.getName();

    }
    //IMAGE FUNCTIONS
    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData() !=null){
            mImageUri = data.getData();
            displayImageView.setVisibility(View.VISIBLE);
            Picasso.get().load(mImageUri).into(displayImageView);

        }
    }
    /* CAMERA UPLOAD
    private void cameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setType("image/*");
        startActivityForResult(intent,CAMERA_REQUEST);
    }*/
    private void matcher(final String itemName, String description,final String key,final String status){
        final List<items> items = new ArrayList<>();
        final List<String> allItemNames = new ArrayList<>();
        final List<String> allItemId = new ArrayList<>();

        DatabaseReference matcherRef = FirebaseDatabase.getInstance().getReference().child("matcher").child("matchedItem");
        final DatabaseReference matcherItem = matcherRef.push();



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("items");
        Query similarItemName = ref.orderByChild("status").equalTo("Lost");
        similarItemName.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()){
                            items Items = itemSnapshot.getValue(items.class);
                            allItemNames.add(Items.getitemName());
                            allItemId.add(Items.getItemID());
                        }

                        //add result into array list
                        for (int i = 0 ; i<allItemNames.size(); i++) {

                            if(!allItemNames.get(i).toUpperCase().contains(itemName.toUpperCase()) || !itemName.toUpperCase().contains(allItemNames.get(i).toUpperCase()) ){
                                continue;

                            }else{
                                Log.i("INFO",key + " " + itemName + ": "+ allItemId.get(i));
                                //ADDITIONS FOR MATCHING ALGO
                                Map<String,Object> Matching = new HashMap<>();
                                Matching.put("itemName", itemName);
                                Matching.put("notifiedOldPosterId", allItemId.get(i));
                                Matching.put("newItemKey",key);
                                Matching.put("status",status);
                                 DatabaseReference matcherItemRef = matcherItem.push();
                                 matcherItemRef.setValue(Matching);
                                /*matcherItemRef.child("itemName").setValue(itemName);
                                matcherItemRef.child("notifiedOldPosterId").setValue(allItemId.get(i));
                                matcherItemRef.child("newItemKey").setValue(key);
                                matcherItemRef.child("status").setValue(status);*/
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

    }



}//ACTIVITY END
