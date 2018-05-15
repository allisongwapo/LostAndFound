package noelanthony.com.lostandfoundfinal.Util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.core.Context;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import noelanthony.com.lostandfoundfinal.ChatMessage;
import noelanthony.com.lostandfoundfinal.ChatMessagesActivity;
import noelanthony.com.lostandfoundfinal.R;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private static final int ITEM_TYPE_SENT = 0;
    private static final int ITEM_TYPE_RECEIVED = 1;
    private List<ChatMessage> mMessagesList;
    private Context mContext;


    public MessagesAdapter(List<ChatMessage> mMessagesList, ChatMessagesActivity chatMessagesActivity) {
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView messageTextView;

        public View Layout;
        public ViewHolder(View v){
            super(v);
            Layout = v;
            messageTextView = (TextView) v.findViewById(R.id.chatMsgTextView);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (mMessagesList.get(position).getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return ITEM_TYPE_SENT;
        } else {
            return ITEM_TYPE_RECEIVED;
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = null;
        if (viewType == ITEM_TYPE_SENT) {
            v = LayoutInflater.from(v.getContext()).inflate(R.layout.sent_msg_row, null);
        } else if (viewType == ITEM_TYPE_RECEIVED) {
            v = LayoutInflater.from(v.getContext()).inflate(R.layout.received_msg_row, null);
        }
        return new ViewHolder(v);// view holder for header items


}

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }


}
