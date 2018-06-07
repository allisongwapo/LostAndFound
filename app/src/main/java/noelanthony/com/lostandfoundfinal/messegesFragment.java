package noelanthony.com.lostandfoundfinal;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    ArrayList<ChatMessage> userList;
    HashSet<ChatMessage> set;
    ArrayList<ChatMessage> result;
    //List<UserInformation> mUsersList = new ArrayList<>();
    private UserAdapter adapter=null;
    private LinearLayout mClick;
    private String mReceiverId;
    private String mReceiverName,userID,name;
    private int count;
    private FirebaseAuth mAuth;
    private DatabaseReference dbUsserReference,mDatabase, dbLostReference;
    private String mSenderId;


    View myView;
    private View itemOnClick;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.messeges_layout, container, false);

        Firebase.setAndroidContext(getActivity());
        ((newsFeedActivity) getActivity())
                .setActionBarTitle("Messages");


        userListView = myView.findViewById(R.id.userListView);
        userListView.setTextFilterEnabled(true);
        itemOnClick = myView.findViewById(R.id.itemOnClick);
        //userListView = myView.findViewById(R.id.itemOnClick);




        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        userList = new ArrayList<>();
        /*set = new HashSet<>(userList);
        result = new ArrayList<>(set);*/

        //initialize firebase dn
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://lostandfoundfinal.firebaseio.com/");
        dbLostReference = mDatabase.child("Messages");


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

       Query ApprovedQuery = dbLostReference.orderByChild("Messages");
        final ArrayList<ChatMessage> senderIdList = new ArrayList<>();

        ApprovedQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                //Set<ChatMessage> hs = new HashSet<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                    if(chatMessage.getReceiverId()!=null
                            && chatMessage.getSenderId().equals(mSenderId)){
                        mReceiverName = chatMessage.getReceiverName();
                        mReceiverId = chatMessage.getReceiverId();
                        userList.add(chatMessage);

                    }


                        //}


                   // for (int i = 0; i < senderIdList.size(); i++) {
                        //Toast.makeText(getActivity(), senderIdList.toString(), Toast.LENGTH_LONG).show();
                   // }

                    /*List newList = new ArrayList(new LinkedHashSet(userList));
                    Iterator it = newList.iterator();
                    while(it.hasNext()){
                        //Toast.makeText(getActivity(), it.next(), Toast.LENGTH_LONG).show();
                        Log.i("ASDDAS: ",(it.next()).toString());
                        userList.add(newList);
                    }*/
                }
               /*Set<ChatMessage> hs = new HashSet<>();
                hs.addAll(userList);
                userList.clear();
                userList.addAll(hs);*/
                //Log.i("assadf: " ,result.toString());

               // Log.i("Sender ID List: ", senderIdList.get(0));
                if(getActivity()!=null) {
                    for(int i = 0; i < userList.size(); i++) {
                        for (int x = 0; x < userList.size(); x++) {
                            if (userList.get(i)!= userList.get(x)) {

                            }else{
                                userList.remove(i);
                            }
                        }
                    }

                           adapter = new UserAdapter(getActivity(), userList);
                           userListView.setAdapter(adapter);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
   /*itemOnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ChatMessagesActivity.class);
                intent.putExtra("name",mReceiverName);
                intent.putExtra("id",mReceiverId );
                startActivity(intent);

            }
        });*/






        return myView;
    }
}
