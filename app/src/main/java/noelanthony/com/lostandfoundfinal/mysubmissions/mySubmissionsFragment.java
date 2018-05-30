package noelanthony.com.lostandfoundfinal.mysubmissions;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import noelanthony.com.lostandfoundfinal.R;
import noelanthony.com.lostandfoundfinal.navmenu.newsFeedActivity;
import noelanthony.com.lostandfoundfinal.newsfeed.items;
import noelanthony.com.lostandfoundfinal.newsfeed.onItemClickActivity;

/**
 * Created by Noel on 10/03/2018.
 */

public class mySubmissionsFragment extends Fragment{

    ListView itemListView;
    List<items> itemList;
    //Context applicationContext = MainActivity.getContextOfApplication();

    private FirebaseAuth mAuth;
    private String userID;
    private DatabaseReference dbLostReference,mDatabase;

    //FOR ON ITEM CLICK LIST
    public static final String KEY_ITEM_NAME="item_name";
    public static final String KEY_STATUS="item_status";
    public static final String KEY_DATE = "item_date_time";
    public static final String KEY_LOCATION = "item_location";
    public static final String KEY_DESCRIPTION = "item_description";
    public static final String KEY_POSTER = "item_poster";
    public static final String KEY_IMAGE_ID= "item_image_id";
    public static final String KEY_USER_ID= "item_uid";
    public static final String KEY_ITEM_ID= "item_id";


    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.mysubmissions_layout, container, false);

        Firebase.setAndroidContext(getActivity());
        ((newsFeedActivity) getActivity())
                .setActionBarTitle("My Submissions");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        itemListView =  myView.findViewById(R.id.itemListView);;
        itemList = new ArrayList<>();
        //initialize firebase dn
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://lostandfoundfinal.firebaseio.com/");
        dbLostReference= mDatabase.child("items");
        Query UserSubmissionsOnly = dbLostReference.orderByChild("uid").equalTo(userID); //only displays user submissions
        UserSubmissionsOnly.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //gets all children
                itemList.clear();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()){
                    items Items = itemSnapshot.getValue(items.class);
                    itemList.add(0,Items);
                }
                if(getActivity()!=null) {

                    final mySubmissionsList adapter = new mySubmissionsList(getActivity(), itemList);
                    if (adapter.getCount() == 0){
                        Toast.makeText(getActivity(), "No Submissions", Toast.LENGTH_SHORT).show();
                    }
                    //to order by descending
                    itemListView.setAdapter(adapter);
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
                            intent.putExtra(KEY_ITEM_ID,item.getItemID());
                            intent.putExtra("item_latitude", item.getLatitude());
                            intent.putExtra("item_longitude", item.getLongitude());
                            if(item.getStatus().equals("Lost")) {
                                intent.putExtra("visibility", "myLost"); //this extra to to set the setToFoundTextView to Visible
                            }else if(item.getStatus().equals("Found")) {
                                intent.putExtra("visibility", "myFound"); //this extra to to set the setToFoundTextView to Visible
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





        return myView;
    }
}
