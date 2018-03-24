package noelanthony.com.lostandfoundfinal.NewsFeed;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import noelanthony.com.lostandfoundfinal.LoginRegister.MainActivity;
import noelanthony.com.lostandfoundfinal.R;

/**
 * Created by Noel on 16/02/2018.
 */

public class newsfeedFragment extends Fragment {

    ListView itemListView;
    List<items> itemList;
    Context applicationContext = MainActivity.getContextOfApplication();


    View myView;

    private Button submitLostBtn;
    private Button foundItemBtn;
    private DatabaseReference dbLostReference,mDatabase,dbFoundReference;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.newsfeed_layout, container, false);

        Firebase.setAndroidContext(applicationContext);
        itemListView =  myView.findViewById(R.id.itemListView);;
        submitLostBtn = myView.findViewById(R.id.submitFoundBtn);
        foundItemBtn = myView.findViewById(R.id.foundItemBtn);
        itemList = new ArrayList<>();
        //initialize firebase dn
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://lostandfoundfinal.firebaseio.com/");
        dbLostReference= mDatabase.child("items").child("lostItems");
        dbFoundReference=mDatabase.child("items").child("foundItems");
        Query lostDateQuery = dbLostReference.orderByChild("dateSubmitted");
        Query foundDateQuery = dbFoundReference.orderByChild("dateSubmitted");
        lostDateQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //gets all children

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()){
                    items Items = itemSnapshot.getValue(items.class);
                    itemList.add(Items);
                }
                itemAdapter adapter = new itemAdapter(getActivity() ,itemList);
                itemListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        foundDateQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //gets all children

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()){
                    items Items = itemSnapshot.getValue(items.class);
                    itemList.add(Items);

                }
                itemAdapter adapter = new itemAdapter(getActivity() ,itemList);
                itemListView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        submitLostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(applicationContext,submitLostItemActivity.class);
                startActivity(startIntent);
            }
        });
        foundItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(applicationContext,submitFoundItemActivity.class);
                startActivity(startIntent);
            }
        });
        return myView;
    }

}//END

/*NOTES Match 25,2018
AND NEWS FEED KAY DAPAT LOST OG FOUND DILI LOST DAYON FOUND*/
