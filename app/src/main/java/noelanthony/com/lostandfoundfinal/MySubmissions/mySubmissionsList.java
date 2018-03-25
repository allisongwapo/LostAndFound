package noelanthony.com.lostandfoundfinal.MySubmissions;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import noelanthony.com.lostandfoundfinal.R;

/**
 * Created by Noel on 25/03/2018.
 */

public class mySubmissionsList extends ArrayAdapter<myItems> {

    private Activity context;
    private List<myItems> myItemsList;

    public mySubmissionsList(Activity context, List<myItems> myItemsList){
        super(context, R.layout.mysubmissions_listview,myItemsList);
        this.context = context;
        this.myItemsList = myItemsList;
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

        //SET ITEM VALUES HERE
        myItems Items = myItemsList.get(position);

        itemNameTextView.setText(Items.getitemName());
        datetimeTextView.setText(Items.getdateSubmitted());
        locationTextView.setText(Items.getlastSeenLocation());
        posterTextView.setText(Items.getPoster());
        //itemImageView.setImageResource(R.drawable.flashdrive);
        return rowView;

    }
}
