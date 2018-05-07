package noelanthony.com.lostandfoundfinal.NewsFeed;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import noelanthony.com.lostandfoundfinal.LoginRegister.MainActivity;
import noelanthony.com.lostandfoundfinal.R;

/**
 * Created by Noel on 16/02/2018.
 */

public class itemAdapter extends ArrayAdapter<items> {

    private Activity context;
    private List<items> itemList;
    private DatabaseReference mDatabase,nameRef;
    private FirebaseAuth mAuth;
    private String userID,status;
    Context applicationContext = MainActivity.getContextOfApplication();

    public itemAdapter(Activity context, List<items> itemList) {
        super(context, R.layout.itemslv_layout, itemList);
        this.context = context;
        this.itemList = itemList;
    }
    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.itemslv_layout, null, true);

        TextView itemNameTextView = rowView.findViewById(R.id.itemNameTextView);
        TextView datetimeTextView = rowView.findViewById(R.id.datetimeTextView);
        TextView locationTextView = rowView.findViewById(R.id.locationTextView);
        TextView posterTextView = rowView.findViewById(R.id.posterTextView);
        //TextView statusTextView = rowView.findViewById(R.id.statusTextView);
        ImageView itemImageView = rowView.findViewById(R.id.itemImageView);

        //SET ITEM VALUES HERE
        items Items = itemList.get(position);

        itemNameTextView.setText(Items.getitemName());
        datetimeTextView.setText(Items.getdateSubmitted());
        locationTextView.setText(Items.getlastSeenLocation());
        posterTextView.setText("Posted By " + Items.getPoster());
        //statusTextView.setText(Items.getStatus());

        RequestOptions options = new RequestOptions();
        options.fitCenter();
        if(Items.getImageID()==null){
            Glide.with(applicationContext).load(R.mipmap.ic_noimage).apply(options).into(itemImageView);
        }else {
            Glide.with(applicationContext).load(Items.getImageID()).into(itemImageView); // IMAGE VIEW
        }
       /* Picasso.get()
                .load(Items.getImageID())
                .fit().centerCrop()
                .into(itemImageView);
                PICASSO DOESNT WORK*/

        //itemImageView.setImageResource(R.drawable.flashdrive);

        //FOR COLOR CHANGING OF LIST VIEW BASED ON LOST OR FOUND STATUS
        status = Items.getStatus();
        if( status.equals("Found")){
            rowView.setBackgroundColor(context.getResources().getColor(R.color.foundItemColorApproved));
        } else if(status.equals("Lost")){
            rowView.setBackgroundColor(context.getResources().getColor(R.color.lostItemColorApproved));
        }
        //END COLOR CHANGER
        return rowView;
    }


}//END OF CLASS

