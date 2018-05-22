package noelanthony.com.lostandfoundfinal.newsfeed;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import noelanthony.com.lostandfoundfinal.R;
import noelanthony.com.lostandfoundfinal.navmenu.newsFeedActivity;

/**
 * Created by Noel on 16/02/2018.
 */

public class newsfeedFragment extends Fragment {

    ListView itemListView;
    ArrayList<items> itemList;
    //Context applicationContext = MainActivity.getContextOfApplication();
    View myView;
    private Button submitLostBtn;
    private Button foundItemBtn;
    private EditText theFilterEditText;
    private DatabaseReference dbLostReference,mDatabase;
    private itemAdapter adapter;

    //FOR ON ITEM CLICK LIST
    public static final String KEY_ITEM_NAME="item_name";
    public static final String KEY_STATUS="item_status";
    public static final String KEY_DATE = "item_date_time";
    public static final String KEY_LOCATION = "item_location";
    public static final String KEY_DESCRIPTION = "item_description";
    public static final String KEY_POSTER = "item_poster";
    public static final String KEY_IMAGE_ID= "item_image_id";
    public static final String KEY_USER_ID= "item_uid";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.newsfeed_layout, container, false);



        Firebase.setAndroidContext(getActivity());
        ((newsFeedActivity) getActivity())
                .setActionBarTitle("News Feed");
        /*
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(R.color.startblue);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }
        MyNotificationManager.getInstance(getActivity()).displayNotification("Welcome to News Feed", "Your Local push notification!!");
    */

        //FOR ADMIN NOTIFICATION
       //FirebaseMessaging.getInstance().subscribeToTopic("newsfeed");

        itemListView =  myView.findViewById(R.id.itemListView);
        itemListView.setTextFilterEnabled(true);
        submitLostBtn = myView.findViewById(R.id.submitFoundBtn);
        foundItemBtn = myView.findViewById(R.id.foundItemBtn);
        theFilterEditText = myView.findViewById(R.id.theFilterEditText);


        itemList = new ArrayList<>();
        //initialize firebase dn
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://lostandfoundfinal.firebaseio.com/");
        dbLostReference= mDatabase.child("items");

       // Query DescendingDateQuery = dbLostReference.orderByChild("dateSubmitted"); //Orders items by Descending date
        Query ApprovedQuery = dbLostReference.orderByChild("approvalStatus").equalTo(1); //Only displays approved items 1

        ApprovedQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //gets all children
                itemList.clear(); //clear listview before populate

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()){
                    items Items = itemSnapshot.getValue(items.class);
                    itemList.add(0,Items);//remove 0 if ma guba
                }
                if(getActivity()!=null) {
                      adapter = new itemAdapter(getActivity(), itemList);
                   // Collections.reverse(itemList); //to order by descending
                    itemListView.setAdapter(adapter);
                    theFilterEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            adapter.getFilter().filter(s);
                            Log.d("Constants.TAG", "*** Search value changed: " + s.toString());
                           // adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void afterTextChanged(Editable s) {


                        }
                    });

                    itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            items item =  adapter.getItem(position);

                            Intent intent = new Intent(getActivity(),onItemClickActivity.class);
                            intent.putExtra(KEY_ITEM_NAME,item.getitemName());
                            intent.putExtra(KEY_STATUS,item.getStatus());
                            intent.putExtra(KEY_DATE,item.getdateSubmitted());
                            intent.putExtra(KEY_LOCATION,item.getlocationDescription());
                            intent.putExtra(KEY_DESCRIPTION,item.getDescription());
                            intent.putExtra(KEY_POSTER,item.getPoster());
                            intent.putExtra(KEY_IMAGE_ID,item.getImageID());
                            intent.putExtra(KEY_USER_ID,item.getUid());
                            intent.putExtra("item_latitude", item.getLatitude());
                            intent.putExtra("item_longitude", item.getLongitude());

                            if(item.getStatus().equals("Lost")) {
                                intent.putExtra("visibility", "newsLost"); //this extra to to set the setToFoundTextView to Visible
                            }else if(item.getStatus().equals("Found")) {
                                intent.putExtra("visibility", "newsFound"); //this extra to to set the setToFoundTextView to Visible
                            }
                            startActivity(intent);
                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        itemListView.post(new Runnable() {
            @Override
            public void run() {
                itemListView.smoothScrollToPosition(0);
            }
        });

        submitLostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getActivity(),submitLostItemActivity.class);
                startActivity(startIntent);
            }
        });
        foundItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getActivity(),submitFoundItemActivity.class);
                startActivity(startIntent);
            }
        });




        return myView;
    }



}//END

/*NOTES Match 25,2018*/
