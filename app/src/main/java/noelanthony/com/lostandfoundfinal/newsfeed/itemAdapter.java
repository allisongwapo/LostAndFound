package noelanthony.com.lostandfoundfinal.newsfeed;

import android.app.Activity;
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

import noelanthony.com.lostandfoundfinal.R;

/**
 * Created by Noel on 16/02/2018.
 */

public class itemAdapter extends ArrayAdapter<items> implements Filterable {

    private Activity context;
    private ArrayList<items> itemList = null;
    private ArrayList<items> filterList = null;
   // private Context applicationContext = MainActivity.getContextOfApplication();
    private ItemsFilter mItemsFilter = new ItemsFilter();
    //Two data sources, the original data and filtered data

    public itemAdapter(Activity context, ArrayList<items> itemList) {
        super(context, R.layout.itemslv_layout, itemList);
        this.context = context;
        this.itemList = itemList;
        this.filterList =  itemList;

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
        locationTextView.setText(Items.getlocationDescription());
        posterTextView.setText("Posted By " + Items.getPoster());
        //statusTextView.setText(Items.getStatus());

        RequestOptions options = new RequestOptions();
        options.fitCenter();
        if(Items.getImageID()==null){
            Glide.with(getContext()).load(R.mipmap.ic_noimagea).apply(options).into(itemImageView);
        }else {
            Glide.with(getContext()).load(Items.getImageID()).into(itemImageView); // IMAGE VIEW
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

            // If the constraint (search string/pattern) is null
            // or its length is 0, i.e., its empty then
            // we just set the `values` property to the
            // original contacts list which contains all of them

            if (constraint == null || constraint.length() == 0 && constraint.equals(isEmpty()) || constraint.toString().isEmpty()) {
                results.count = filterList.size();
                results.values = filterList;
            } else {
                // Some search constraint has been passed
                // so let's filter accordingly
                ArrayList<items> filteredItems = new ArrayList<items>();
                // We'll go through all the contacts and see
                // if they contain the supplied string
                for (items c : itemList) {
                    if (c.getitemName().toUpperCase().contains(constraint.toString().toUpperCase()) || c.getlocationDescription().toUpperCase().contains(constraint.toString().toUpperCase()) || c.getPoster().toUpperCase().contains(constraint.toString().toUpperCase()) || c.getdateSubmitted().toUpperCase().contains(constraint.toString().toUpperCase())) {
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

// Return our FilterResults object
            return results;
        }
/*

            FilterResults results = new FilterResults();

            final List<items> list = filterList;

            int count = list.size();
            final ArrayList<items> nlist = new ArrayList<items>(count);

            for (items c : itemList ) {
                if (c.getitemName().toUpperCase().contains(constraint.toString().toUpperCase()) || c.getlocationDescription().toUpperCase().contains(constraint.toString().toUpperCase()) || c.getPoster().toUpperCase().contains(constraint.toString().toUpperCase()) || c.getdateSubmitted().toUpperCase().contains(constraint.toString().toUpperCase())) {
                    // if `contains` == true then add it
                    // to our filtered list
                    nlist.add(c);
                }
            }
            results.values = nlist;
            results.count = nlist.size();

            return results;

        }
*/





        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

              itemList = (ArrayList<items>) results.values;
             notifyDataSetChanged();
            /*
            itemList = (ArrayList<items>) results.values;
            if (results.count >0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }*/


            /*
            itemList = (ArrayList<items>) results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0, l = itemList.size(); i < l; i++){
                add(itemList.get(i));
            notifyDataSetInvalidated();}
            */
            /*
             itemList = (ArrayList<items>) results.values;
             clear();
             notifyDataSetInvalidated();
             addAll(itemList);
                */


        }
    }
    @Override
    public Filter getFilter() {
        if(mItemsFilter==null) {
            mItemsFilter = new ItemsFilter();
        }
        return mItemsFilter;
    }

}//END OF CLASS

