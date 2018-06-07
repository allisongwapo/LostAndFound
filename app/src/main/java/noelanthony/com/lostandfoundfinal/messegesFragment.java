package noelanthony.com.lostandfoundfinal;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import noelanthony.com.lostandfoundfinal.Util.UserAdapter;
import noelanthony.com.lostandfoundfinal.navmenu.newsFeedActivity;
import noelanthony.com.lostandfoundfinal.newsfeed.onItemClickActivity;
import noelanthony.com.lostandfoundfinal.profile.UserInformation;

/**
 * Created by Noel on 07/05/2018.
 */

public class messegesFragment extends Fragment {
    ListView userListView;
    ArrayList<UserInformation> userList;
    HashSet<ChatMessage> set;
    ArrayList<String>  result = new ArrayList<>();
    //List<UserInformation> mUsersList = new ArrayList<>();
    private UserAdapter adapter=null;
    private LinearLayout mClick;
    private String mReceiverId;
    private String mReceiverName,userID,name;
    private int count;
    private FirebaseAuth mAuth;
    private DatabaseReference dbUsserReference,mDatabase, dbLostReference;
    private String mSenderId;
    private String uid;


    View myView;

    public static final String KEY_USER_NAME="item_poster";
    public static final String KEY_USER_ID="item_uid";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.messeges_layout, container, false);

        Firebase.setAndroidContext(getActivity());
        ((newsFeedActivity) getActivity())
                .setActionBarTitle("Messages");


        userListView = myView.findViewById(R.id.userListView);
        userListView.setTextFilterEnabled(true);
        //userListView = myView.findViewById(R.id.itemOnClick);




        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        userList = new ArrayList<>();
        /*set = new HashSet<>(userList);
        result = new ArrayList<>(set);*/

        //initialize firebase dn
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://lostandfoundfinal.firebaseio.com/");
        dbLostReference = mDatabase.child("users");


        String sender = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mSenderId = sender;
        //mReceiverId = sender;

      /*  userListView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ChatMessagesActivity.class);
                intent.putExtra("name",mReceiverName);
                intent.putExtra("id",mReceiverId );
                startActivity(intent);
            }
        });
*/
       Query ApprovedQuery = dbLostReference.orderByChild("messages");
        final ArrayList<ChatMessage> senderIdList = new ArrayList<>();

        ApprovedQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                //Set<ChatMessage> hs = new HashSet<>();
                for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                    String key = uniqueKeySnapshot.getKey();
                    result.add(key);
                    UserInformation userInformation = uniqueKeySnapshot.getValue(UserInformation.class);
                        if (userInformation.getName() != null
                                && !key.contains(userID)) {
                            userList.add(userInformation);

                        }
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
    userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            UserInformation userInformation = adapter.getItem(position);

            Intent intent = new Intent(getActivity(), ChatMessagesActivity.class);
            intent.putExtra(KEY_USER_NAME, userInformation.getName());
            intent.putExtra(KEY_USER_ID, userInformation.getUserId());
            startActivity(intent);

        }
    });






        return myView;
    }
}
