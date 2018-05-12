package noelanthony.com.lostandfoundfinal.NewsFeed;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import noelanthony.com.lostandfoundfinal.LoginRegister.MainActivity;
import noelanthony.com.lostandfoundfinal.R;

/**
 * Created by Noel on 16/02/2018.
 */

public class itemAdapter extends ArrayAdapter<items> implements Filterable {

    private Activity context;
    private ArrayList<items> itemList;
    private ArrayList<items> filterList;
    private Context applicationContext = MainActivity.getContextOfApplication();
    private ItemsFilter mItemsFilter;
    //Two data sources, the original data and filtered data

    public itemAdapter(Activity context, ArrayList<items> itemList) {
        super(context, R.layout.itemslv_layout, itemList);
        this.context = context;
        this.itemList = itemList;
        this.filterList = itemList;


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
        String status = Items.getStatus();
        if( status.equals("Found")){
            rowView.setBackgroundColor(context.getResources().getColor(R.color.foundItemColor));
        } else if(status.equals("Lost")){
            rowView.setBackgroundColor(context.getResources().getColor(R.color.lostItemColor));
        }
        //END COLOR CHANGER
        return rowView;
    }

    private class ItemsFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // Create a FilterResults object
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                constraint = constraint.toString().toUpperCase();
                List<items> filters = new ArrayList<>();
                results.count = itemList.size();
                results.values = itemList;

            }else {
                // Some search constraint has been passed
                // so let's filter accordingly
                ArrayList<items> filteredItems = new ArrayList<>();
                // We'll go through all the contacts and see
                // if they contain the supplied string
                for (items c : itemList ) {
                    if (c.getitemName().toUpperCase().contains( constraint.toString().toUpperCase() )) {
                        // if `contains` == true then add it
                        // to our filtered list
                        filteredItems.add(c);
                    }
                }
                // Finally set the filtered values and size/count
                results.values = filteredItems;
                results.count = filteredItems.size();
            }
            Log.e("VALUES", results.values.toString());

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0) {
                itemList.clear();
                notifyDataSetInvalidated();
            } else {
                itemList = (ArrayList<items>) results.values;
                notifyDataSetChanged();
            }

        }
    }
    @Override
    public Filter getFilter() {
        if(mItemsFilter==null)
            mItemsFilter = new ItemsFilter();

        return mItemsFilter;
    }
}//END OF CLASS

