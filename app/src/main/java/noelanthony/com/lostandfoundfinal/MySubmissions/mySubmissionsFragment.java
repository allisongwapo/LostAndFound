package noelanthony.com.lostandfoundfinal.MySubmissions;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

import noelanthony.com.lostandfoundfinal.LoginRegister.MainActivity;
import noelanthony.com.lostandfoundfinal.NewsFeed.items;
import noelanthony.com.lostandfoundfinal.R;

/**
 * Created by Noel on 10/03/2018.
 */

public class mySubmissionsFragment extends Fragment{

    ListView itemListView;
    List<items> itemList;
    Context applicationContext = MainActivity.getContextOfApplication();

    private FirebaseAuth mAuth;
    private String userID;
    private DatabaseReference dbLostReference,mDatabase;
    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.mysubmissions_layout, container, false);

        Firebase.setAndroidContext(applicationContext);
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

                    mySubmissionsList adapter = new mySubmissionsList(getActivity(), itemList);
                    //to order by descending
                    itemListView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return myView;
    }
}
