package noelanthony.com.lostandfoundfinal.mysubmissions;

import android.app.Activity;
import android.content.Context;
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

import java.util.List;

import noelanthony.com.lostandfoundfinal.loginregister.MainActivity;
import noelanthony.com.lostandfoundfinal.newsfeed.items;
import noelanthony.com.lostandfoundfinal.R;

/**
 * Created by Noel on 25/03/2018.
 */

public class mySubmissionsList extends ArrayAdapter<items> {

    private Activity context;
    private List<items> itemList;
    private int approvalStatus;
    private String status;
    private Context applicationContext = MainActivity.getContextOfApplication();

    public mySubmissionsList(Activity context, List<items> itemList){
        super(context, R.layout.mysubmissions_listview, itemList);
        this.context = context;
        this.itemList = itemList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mysubmissions_listview,null,true);

        TextView itemNameTextView = rowView.findViewById(R.id.itemNameTextView);
        TextView datetimeTextView = rowView.findViewById(R.id.datetimeTextView);
        TextView locationTextView = rowView.findViewById(R.id.locationTextView);
        TextView posterTextView = rowView.findViewById(R.id.posterTextView);
        ImageView itemImageView = rowView.findViewById(R.id.itemImageView);
        ImageView approvalStatusImageView = rowView.findViewById(R.id.approvalStatusImageView);
        TextView approvalStatusTextView = rowView.findViewById(R.id.approvalStatusTextView);

        //SET ITEM VALUES HERE
        items Items = itemList.get(position);
       
        itemNameTextView.setText(Items.getitemName());
        datetimeTextView.setText(Items.getdateSubmitted());
        locationTextView.setText(Items.getlocationDescription());
        posterTextView.setText(Items.getPoster());
        RequestOptions options = new RequestOptions();
        options.fitCenter();
        if(Items.getImageID()==null){
            Glide.with(getContext()).load(R.mipmap.ic_noimage).apply(options).into(itemImageView);
        }else {
            Glide.with(getContext()).load(Items.getImageID()).into(itemImageView); // IMAGE VIEW
        }
        //itemImageView.setImageResource(R.drawable.flashdrive);


        //PUT APPROVAL NEED
        status = Items.getStatus();
        approvalStatus = Items.getApprovalStatus();
        if( status.equals("Found") && approvalStatus==1){
            rowView.setBackgroundColor(context.getResources().getColor(R.color.foundItemColorApproved));
            Glide.with(getContext()).load(R.drawable.ic_check_circle_black_24dp).into(approvalStatusImageView);
            approvalStatusTextView.setText("Approved");
        } else if(status.equals("Lost") && approvalStatus==1){
            rowView.setBackgroundColor(context.getResources().getColor(R.color.lostItemColorApproved));
            Glide.with(getContext()).load(R.drawable.ic_check_circle_black_24dp).into(approvalStatusImageView);
            approvalStatusTextView.setText("Approved");
        }else if(status.equals("Lost then Found") && approvalStatus==1){
            rowView.setBackgroundColor(context.getResources().getColor(R.color.lostthenfoundItemColor));
            Glide.with(getContext()).load(R.drawable.ic_clear_black_24dp).into(approvalStatusImageView);
            approvalStatusTextView.setText("Approved");
        } else if(status.equals("Found") && approvalStatus==0){
            rowView.setBackgroundColor(context.getResources().getColor(R.color.foundItemColorPending));
            Glide.with(getContext()).load(R.drawable.ic_loop_black_24dp).into(approvalStatusImageView);
            approvalStatusTextView.setText("Pending Approval");
        } else if(status.equals("Lost") && approvalStatus==0){
            rowView.setBackgroundColor(context.getResources().getColor(R.color.lostItemColorPending));
            Glide.with(getContext()).load(R.drawable.ic_loop_black_24dp).into(approvalStatusImageView);
            approvalStatusTextView.setText("Pending Approval");
        }else if(status.equals("Lost then Found") && approvalStatus==0){
            rowView.setBackgroundColor(context.getResources().getColor(R.color.lostthenfoundItemColor));
            Glide.with(getContext()).load(R.drawable.ic_clear_black_24dp).into(approvalStatusImageView);
            approvalStatusTextView.setText("Pending Approval");
        }else if(status.equals("Found") && approvalStatus==2){
            rowView.setBackgroundColor(context.getResources().getColor(R.color.foundItemColorDeclined));
            Glide.with(getContext()).load(R.drawable.ic_clear_black_24dp).into(approvalStatusImageView);
            approvalStatusTextView.setText("Declined");
        }else if(status.equals("Lost") && approvalStatus==2){
            rowView.setBackgroundColor(context.getResources().getColor(R.color.lostItemColorDeclined));
            Glide.with(getContext()).load(R.drawable.ic_clear_black_24dp).into(approvalStatusImageView);
            approvalStatusTextView.setText("Declined");
        }else if(status.equals("Lost then Found") && approvalStatus==2){
            rowView.setBackgroundColor(context.getResources().getColor(R.color.lostthenfoundItemColor));
            Glide.with(getContext()).load(R.drawable.ic_clear_black_24dp).into(approvalStatusImageView);
            approvalStatusTextView.setText("Declined");
        }



        return rowView;

    }

}


