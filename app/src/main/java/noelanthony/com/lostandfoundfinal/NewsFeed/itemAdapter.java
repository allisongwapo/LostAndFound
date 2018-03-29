package noelanthony.com.lostandfoundfinal.NewsFeed;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

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

    public itemAdapter(Activity context, List<items> itemList) {
        super(context, R.layout.itemslv_layout, itemList);
        this.context = context;
        this.itemList = itemList;
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
        TextView statusTextView = rowView.findViewById(R.id.statusTextView);
        ImageView itemImageView = rowView.findViewById(R.id.itemImageView);


        //SET ITEM VALUES HERE
        items Items = itemList.get(position);

        itemNameTextView.setText(Items.getitemName());
        datetimeTextView.setText(Items.getdateSubmitted());
        locationTextView.setText(Items.getlastSeenLocation());
        posterTextView.setText("Posted By " + Items.getPoster());
        statusTextView.setText(Items.getStatus());
        //itemImageView.setImageResource(R.drawable.flashdrive);

        //FOR COLOR CHANGING OF LIST VIEW BASED ON LOST OR FOUND STATUS
        status = Items.getStatus();
        if( status.equals("Found")){
            rowView.setBackgroundColor(context.getResources().getColor(R.color.foundItemColor));
        } else if(status.equals("Lost")){
            rowView.setBackgroundColor(context.getResources().getColor(R.color.lostItemColor));
        }
        //END COLOR CHANGER
        return rowView;
    }


}//END OF CLASS

