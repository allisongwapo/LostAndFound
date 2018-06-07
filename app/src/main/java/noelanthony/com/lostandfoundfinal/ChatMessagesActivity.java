package noelanthony.com.lostandfoundfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.List;

import noelanthony.com.lostandfoundfinal.Util.MessagesAdapter;
import noelanthony.com.lostandfoundfinal.profile.UserInformation;

public class ChatMessagesActivity extends AppCompatActivity {
    private RecyclerView mChatsRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private EditText mMessageEditText;
    private ImageButton mSendImageButton;
    private DatabaseReference mMessagesDBRef, mMessagesDBREF;
    private DatabaseReference mUsersRef;
    private List<ChatMessage> mMessagesList = new ArrayList<>();
    private List<UserInformation> mUsersList = new ArrayList<>();
    private MessagesAdapter adapter=null;
    private String mReceiverId,mmReceiverId;
    private String mReceiverName,mmReceiverName;
    private String mSenderName;
    private String sender;
    private String convoBetweenUsers,convo;

    private FirebaseAuth mAuth;
    private DatabaseReference dbReference,mDatabase;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);


        //initialize the views
        mChatsRecyclerView = findViewById(R.id.messagesRecyclerView);
        mMessageEditText = findViewById(R.id.messageEditText);
        mSendImageButton = findViewById(R.id.sendMessageImagebutton);
        mChatsRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(true);
        mChatsRecyclerView.setLayoutManager(mLayoutManager);

        //init Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://lostandfoundfinal.firebaseio.com/");
        mUsersRef = mDatabase.child("users");
        mMessagesDBREF = mDatabase.child("Messages");



        //get receiverId from intent
        Intent intent = getIntent();
        if (null!= intent) {
            mReceiverId = intent.getStringExtra("item_uid");
            mReceiverName = intent.getStringExtra("item_poster");
            mmReceiverName = intent.getStringExtra("name");
            mmReceiverId = intent.getStringExtra("id");
        }

        convoBetweenUsers = mReceiverId+userID;
        convo = userID+mReceiverId;
        mMessagesDBRef = FirebaseDatabase.getInstance().getReference().child("Messages");
        String receiverName1 =mmReceiverName;
        String receiverName = mReceiverName;
        if(receiverName!=null || receiverName1!=null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(receiverName);
        }


        sender = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query ApprovedQuery = mUsersRef.orderByKey().equalTo(sender);

        ApprovedQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //gets all children
                mUsersList.clear(); //clear listview before populate

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            UserInformation userInformation = snapshot.getValue(UserInformation.class);
                            mUsersList.add(userInformation);
                            mSenderName = userInformation.getName();
                        }


                    }
            /*    }

            }*/

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /**listen to send message imagebutton click**/

        mSendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mMessageEditText.getText().toString();
                String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String status = "NOT SEEN";
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateTime = dateFormat.format(new Date()); // Find todays date

                if (message.isEmpty()) {
                    Toast.makeText(ChatMessagesActivity.this, "You must enter a message", Toast.LENGTH_SHORT).show();
                } else {
                    //message is entered, send
                    sendMessageToFirebase(message, senderId, mReceiverId, mReceiverName, mSenderName, status, currentDateTime );
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        /**Query and populate chat messages**/
        querymessagesBetweenThisUserAndClickedUser();


        /**sets title bar with recepient name**/
        //queryRecipientName();
    }


    private void sendMessageToFirebase(String message, String senderId, String receiverId, String receiverName, String senderName, String status, String currentDateTime ) {
        mMessagesList.clear();

        ChatMessage newMsg = new ChatMessage(message, senderId, receiverId,  receiverName,senderName, status, currentDateTime);

        mMessagesDBRef.push().setValue(newMsg).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    //error
                    Toast.makeText(ChatMessagesActivity.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChatMessagesActivity.this, "Message sent successfully!", Toast.LENGTH_SHORT).show();
                    mMessageEditText.setText(null);
                    hideSoftKeyboard();
                }
            }
        });


    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    private void querymessagesBetweenThisUserAndClickedUser(){
        Query SelectQuery = mMessagesDBRef.orderByChild("Messages");
        SelectQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMessagesList.clear();
                String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String receiverId = mReceiverId;
                    String receiverId1 = mmReceiverId;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                        try {
                            if (receiverId!= null
                                    && chatMessage.getSenderId().contentEquals(senderId)
                                    && chatMessage.getReceiverId().contentEquals(receiverId)
                                    || chatMessage.getSenderId().contentEquals(receiverId)
                                    && chatMessage.getReceiverId().contentEquals(senderId)) {
                                mMessagesList.add(chatMessage);
                            }else if(receiverId1!= null
                                    && chatMessage.getSenderId().contentEquals(senderId)
                                    && chatMessage.getReceiverId().contentEquals(receiverId1)
                                    || chatMessage.getSenderId().contentEquals(receiverId1)
                                    && chatMessage.getReceiverId().contentEquals(senderId)) {
                                mMessagesList.add(chatMessage);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }


                /**populate messages**/
                populateMessagesRecyclerView();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void populateMessagesRecyclerView() {
        adapter = new MessagesAdapter(mMessagesList, this);
        mChatsRecyclerView.setAdapter(adapter);
    }


}
