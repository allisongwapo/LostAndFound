package noelanthony.com.lostandfoundfinal.admin;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import noelanthony.com.lostandfoundfinal.loginregister.MainActivity;
import noelanthony.com.lostandfoundfinal.newsfeed.items;
import noelanthony.com.lostandfoundfinal.R;

/**
 * Created by Noel on 30/04/2018.
 */

public class CustomAdapter extends BaseAdapter {
    Activity activity;
    List<items> items;
    LayoutInflater inflater;
    String status;
    Context applicationContext = MainActivity.getContextOfApplication();

    public CustomAdapter(Activity activity) {
        this.activity = activity;
    }

    public CustomAdapter(Activity activity, List<items> items) {
        this.activity = activity;
        this.items = items;

        inflater = activity.getLayoutInflater();
    }
    @Override
    public int getCount(){
        return items.size();
    }

    @Override
    public Object getItem (int i){
        return i;
    }
    @Override
    public long getItemId(int i){
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        ViewHolder holder =null;
        if (view == null){
            view = inflater.inflate(R.layout.approveitemslistview_layout,viewGroup,false);
            holder = new ViewHolder();
            holder.itemNameTextView = view.findViewById(R.id.itemNameTextView);
            holder.datetimeTextView = view.findViewById(R.id.datetimeTextView);
            holder.locationTextView = view.findViewById(R.id.locationTextView);
            holder.posterTextView = view.findViewById(R.id.posterTextView);
            //TextView statusTextView = rowView.findViewById(R.id.statusTextView);
            holder.itemImageView = view.findViewById(R.id.itemImageView);
            holder.checkboxImageView = view.findViewById(R.id.checkboxImageView);

            //SET ITEM VALUES HERE
            items Items = items.get(i);

            holder.itemNameTextView.setText(Items.getitemName());
            holder.datetimeTextView.setText(Items.getdateSubmitted());
            holder.locationTextView.setText(Items.getlocationDescription());
            holder.posterTextView.setText("Posted By " + Items.getPoster());
            //statusTextView.setText(Items.getStatus());
            status = Items.getStatus();
            if( status.equals("Found")){
                view.setBackgroundColor(activity.getResources().getColor(R.color.foundItemColorApproved));
            } else if(status.equals("Lost")){
                view.setBackgroundColor(activity.getResources().getColor(R.color.lostItemColorApproved));
            }

            //FOR DEFAULT IMAGE
            RequestOptions options = new RequestOptions();
            options.fitCenter();
            if(Items.getImageID()==null){
                Glide.with(applicationContext).load(R.mipmap.ic_noimage).apply(options).into(holder.itemImageView);
            }else {
                Glide.with(applicationContext).load(Items.getImageID()).into(holder.itemImageView); // IMAGE VIEW
            }

            view.setTag(holder);
            }else {
            holder =(ViewHolder)view.getTag();
            items item = items.get(i);

            if(item.isSelected()){
                holder.checkboxImageView.setBackgroundResource(R.drawable.ic_check_box_black_24dp);
            }else{
                holder.checkboxImageView.setBackgroundResource(R.drawable.ic_check_box_outline_blank_black_24dp);
            }

        }

        //END COLOR CHANGER
        return view;
    }
    public void updateRecords(List<items> items){
        this.items =items;

        notifyDataSetChanged();
    }
    class ViewHolder{
        TextView itemNameTextView;
        TextView datetimeTextView;
        TextView locationTextView;
        TextView posterTextView;
        //TextView statusTextView = rowView.findViewById(R.id.statusTextView);
        ImageView itemImageView;
        ImageView checkboxImageView;
    }


}
