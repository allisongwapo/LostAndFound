package noelanthony.com.lostandfoundfinal.NewsFeed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import noelanthony.com.lostandfoundfinal.R;

/**
 * Created by Noel on 16/02/2018.
 */

public class
itemAdapter extends ArrayAdapter<items> {

    private final Context context;
    private final ArrayList<items> values;

    public itemAdapter(@NonNull Context context, ArrayList<items> list) {
        super(context, R.layout.itemslv_layout, list);
        this.context = context;
        this.values = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.itemslv_layout, parent, false);

        TextView itemNameTextView = (TextView) rowView.findViewById(R.id.itemNameTextView);
        TextView datetimeTextView = (TextView) rowView.findViewById(R.id.datetimeTextView);
        TextView locationTextView = (TextView) rowView.findViewById(R.id.locationTextView);
        TextView posterTextView = (TextView) rowView.findViewById(R.id.posterTextView);
        ImageView itemImageView = (ImageView) rowView.findViewById(R.id.itemImageView);
        //SET ITEM VALUES HERE
        itemNameTextView.setText(values.get(position).getitemName());
        datetimeTextView.setText(values.get(position).getdateSubmitted());
        locationTextView.setText(values.get(position).getlastSeenLocation());
        posterTextView.setText(values.get(position).getPoster());
        //itemImageView.setImageResource(R.drawable.flashdrive);
        return rowView;
    }
}
