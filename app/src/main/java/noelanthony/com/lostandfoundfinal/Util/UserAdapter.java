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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import noelanthony.com.lostandfoundfinal.ChatMessage;
import noelanthony.com.lostandfoundfinal.R;
import noelanthony.com.lostandfoundfinal.profile.UserInformation;

public class UserAdapter extends ArrayAdapter<ChatMessage> {
    private Activity context;
    private ArrayList<ChatMessage> userList = null;


    public UserAdapter(Activity context, ArrayList<ChatMessage> userList) {
        super(context, R.layout.users_layout, userList);
        this.context = context;
        this.userList = userList;


    }
    @Override
    public int getCount() {
        return userList.size();
       // return mMessagesList.size();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.users_layout, null, true);

        TextView NameTextView = rowView.findViewById(R.id.NameTextView);
        TextView datetimeTextView = rowView.findViewById(R.id.dateandtimeTextView);
        TextView messageTextView = rowView.findViewById(R.id.messageTextView);
        //TextView posterTextView = rowView.findViewById(R.id.posterTextView);
        //TextView statusTextView = rowView.findViewById(R.id.statusTextView);
        ImageView userImageView = rowView.findViewById(R.id.ImageView);
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //SET ITEM VALUES HERE
        ChatMessage users = userList.get(position);


        NameTextView.setText(users.getReceiverName());
        datetimeTextView.setText(users.getTime());
        messageTextView.setText(users.getMessage());


            //messageTextView.setText(users.getMessage());
            //datetimeTextView.setText(users.getTime());



     /*   RequestOptions options = new RequestOptions();
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

