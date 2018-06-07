package noelanthony.com.lostandfoundfinal.Util;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.jar.Attributes;

import noelanthony.com.lostandfoundfinal.ChatMessage;
import noelanthony.com.lostandfoundfinal.R;
import noelanthony.com.lostandfoundfinal.profile.UserInformation;

public class UserAdapter extends ArrayAdapter<ChatMessage> {
    private Activity context;
    private ArrayList<ChatMessage> userList=null;
    /*private List<UserInformation> mUsersList = new ArrayList<>();
    private DatabaseReference mUsersRef;
    private DatabaseReference dbReference,mDatabase;
    private String sender, mSenderName;
*/
    public UserAdapter(Activity context, ArrayList<ChatMessage> userList) {
        super(context, R.layout.users_layout, userList);
        this.context = context;
        this.userList = userList;


    }
    @Override
    public int getCount() {

        return userList.size();

    }



    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.users_layout, null, true);

        final TextView NameTextView = rowView.findViewById(R.id.NameTextView);
        TextView datetimeTextView = rowView.findViewById(R.id.dateandtimeTextView);
        TextView messageTextView = rowView.findViewById(R.id.messageTextView);
        //TextView posterTextView = rowView.findViewById(R.id.posterTextView);
        //TextView statusTextView = rowView.findViewById(R.id.statusTextView);
        //ImageView userImageView = rowView.findViewById(R.id.ImageView);
        //String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //SET ITEM VALUES HERE
        //itemOnClick = findViewById(R.id.itemOnClick);

       /* mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://lostandfoundfinal.firebaseio.com/");
        mUsersRef = mDatabase.child("users");
        sender = FirebaseAuth.getInstance().getCurrentUser().getUid();
*/
      /*  final ChatMessage users = userList.get(position);
        Query ApprovedQuery = mUsersRef.orderByChild("users").equalTo(sender);

        ApprovedQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //gets all children
                mUsersList.clear(); //clear listview before populate
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserInformation userInformation = snapshot.getValue(UserInformation.class);
                    if(userInformation.getName().contentEquals(users.getReceiverName())){
                        mUsersList.add(userInformation);
                        mSenderName = userInformation.getName();
                        //NameTextView.setText(userInformation.getName());
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/



        //ChatMessage users = userList.get(position);
        //ChatMessage name = userList.get(userList.size()-1);
        //String name = users.getReceiverName();
    /*    for(int i = 0; i<userList.size(); i++){
            for(int x= 0; x<userList.size(); x++){
                if(userList.get(i) == userList.get(x)){
                    userList.remove(i);

                }
            }
        }
*/
        ChatMessage users = userList.get(position);
        NameTextView.setText(users.getReceiverName());
        datetimeTextView.setText(users.getTime());
        messageTextView.setText(users.getMessage());



            //messageTextView.setText(users.getMessage());
            //datetimeTextView.setText(users.getTime());



       /*RequestOptions options = new RequestOptions();
        options.fitCenter();
        if(users.getImage()==null){
            Glide.with(getContext()).load(R.mipmap.ic_noimagea).apply(options).into(userImageView);
        }else {
            Glide.with(getContext()).load(users.getImage()).into(userImageView); // IMAGE VIEW
        }*/
       // messageTextView.setText(users.getMessage());
        //messageTextView.setText(users.getlocationDescription());
        //posterTextView.setText("Posted By " + Items.getPoster());
        //statusTextView.setText(Items.getStatus());

        /*RequestOptions options = new RequestOptions();
        options.fitCenter();
        if(Items.getImageID()==null){
            Glide.with(getContext()).load(R.mipmap.ic_noimagea).apply(options).into(itemImageView);
        }else {
            Glide.with(getContext()).load(Items.getImageID()).into(itemImageView); // IMAGE VIEW
        }*/

        //FOR COLOR CHANGING OF LIST VIEW BASED ON LOST OR FOUND STATUS
       /* String status = Items.getStatus();
        if( status.equals("Found")){
            rowView.setBackgroundColor(context.getResources().getColor(R.color.foundItemColor));
        } else if(status.equals("Lost")){
            rowView.setBackgroundColor(context.getResources().getColor(R.color.lostItemColor));
        }
        //END COLOR CHANGER*/
        return rowView;
    }
}

