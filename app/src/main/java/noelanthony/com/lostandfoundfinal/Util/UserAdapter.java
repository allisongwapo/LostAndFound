package noelanthony.com.lostandfoundfinal.Util;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


import noelanthony.com.lostandfoundfinal.ChatMessagesActivity;
import noelanthony.com.lostandfoundfinal.R;
import noelanthony.com.lostandfoundfinal.profile.UserInformation;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private ArrayList<UserInformation> mUserInformationList;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView personNameTxtV;
        public ImageView personImageImgV;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            personNameTxtV = (TextView) v.findViewById(R.id.name);
            personImageImgV = (ImageView) v.findViewById(R.id.image);




        }
    }
    public void add(int position, UserInformation u) {
        mUserInformationList.add(position, u);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mUserInformationList.remove(position);
        notifyItemRemoved(position);
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public UserAdapter(ArrayList<UserInformation> myDataset, Context context) {
        mUserInformationList = myDataset;
        mContext = context;

    }
    // Create new views (invoked by the layout manager)
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.activity_chat_messages, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final UserInformation user = mUserInformationList.get(position);
        holder.personNameTxtV.setText(user.getName());

        Picasso.get().load(user.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.personImageImgV);

        //listen to single view layout click
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send this user id to chat messages activity
                goToUpdateActivity(user.getUserId());
            }
        });


    }

    private void goToUpdateActivity(String uid){
        Intent goToUpdate = new Intent(mContext, ChatMessagesActivity.class);
        goToUpdate.putExtra("users_uid", uid);
        mContext.startActivity(goToUpdate);
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mUserInformationList.size();
    }

}

