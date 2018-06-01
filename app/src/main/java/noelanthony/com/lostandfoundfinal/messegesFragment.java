package noelanthony.com.lostandfoundfinal;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import noelanthony.com.lostandfoundfinal.Util.MessagesAdapter;
import noelanthony.com.lostandfoundfinal.Util.UserAdapter;
import noelanthony.com.lostandfoundfinal.navmenu.newsFeedActivity;
import noelanthony.com.lostandfoundfinal.newsfeed.itemAdapter;
import noelanthony.com.lostandfoundfinal.newsfeed.items;
import noelanthony.com.lostandfoundfinal.profile.UserInformation;

/**
 * Created by Noel on 07/05/2018.
 */

public class messegesFragment extends Fragment {
    ListView userListView;
    ArrayList<ChatMessage> userList;
    List<UserInformation> mUsersList = new ArrayList<>();
    private UserAdapter adapter=null;
    private LinearLayout mClick;
    private String mReceiverId;
    private String mReceiverName;
    private int count;
    private FirebaseAuth mAuth;
    private DatabaseReference dbUsserReference,mDatabase, dbLostReference;
    private String mSenderId;


    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.messeges_layout, container, false);

        Firebase.setAndroidContext(getActivity());
        ((newsFeedActivity) getActivity())
                .setActionBarTitle("Messages");


        userListView = myView.findViewById(R.id.userListView);
        mClick = myView.findViewById(R.id.itemDescriptionTextView);
        //itemListView.setTextFilterEnabled(true);

        userList = new ArrayList<>();
        //initialize firebase dn
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://lostandfoundfinal.firebaseio.com/");
        dbLostReference = mDatabase.child("Messages");


        String sender = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mSenderId = sender;


       Query ApprovedQuery = dbLostReference.orderByChild("message");

        ApprovedQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //gets all children
                userList.clear(); //clear listview before populate
                String senderId = mSenderId;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                    if(chatMessage.getReceiverId().contentEquals(senderId))
                    userList.add(chatMessage);

                }


                if(getActivity()!=null) {
                    adapter = new UserAdapter(getActivity(), userList);
                    userListView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    /*    mClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/






        return myView;
    }
}
