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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import noelanthony.com.lostandfoundfinal.LoginRegister.MainActivity;
import noelanthony.com.lostandfoundfinal.R;

/**
 * Created by Noel on 16/02/2018.
 */

public class newsfeedFragment extends Fragment {

    itemAdapter adapter;
    ListView itemListView;
    FirebaseHelper helper;
    Context applicationContext = MainActivity.getContextOfApplication();



    View myView;
    private Button submitLostBtn;
    private Button foundItemBtn;
    private Firebase rootRef;
    private DatabaseReference dbReference;
    private FirebaseDatabase database;
    final ArrayList<items> list=new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.newsfeed_layout, container, false);

        Firebase.setAndroidContext(applicationContext);
        itemListView = (ListView) myView.findViewById(R.id.itemListView);;
        submitLostBtn = (Button)myView.findViewById(R.id.submitLostBtn);
        foundItemBtn = (Button)myView.findViewById(R.id.foundItemBtn);
        final ArrayList<items> list=new ArrayList<items>();
        //initialize firebase dn
        rootRef= new Firebase("https://lostandfoundfinal.firebaseio.com/");
        database = FirebaseDatabase.getInstance();
        dbReference= database.getReference();//.child("lostItems");
        dbReference.child("lostItems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //gets all children
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                //shake hands with each of them
                for (DataSnapshot child:children) {
                    items value = child.getValue(items.class);
                    list.add(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        helper = new FirebaseHelper(dbReference);

        //Adapter
        adapter= new itemAdapter(applicationContext, helper.retrieve());
        itemListView.setAdapter(adapter);


        submitLostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(applicationContext,submitLostItemActivity.class);
                startActivity(startIntent);
            }
        });
        return myView;
    }
}//END