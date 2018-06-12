package noelanthony.com.lostandfoundfinal.newsfeed;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.util.ArrayList;
import java.util.List;

import noelanthony.com.lostandfoundfinal.R;
import noelanthony.com.lostandfoundfinal.navmenu.newsFeedActivity;

/**
 * Created by Noel on 16/02/2018.
 */

public class newsfeedFragment extends Fragment {

    ListView itemListView;
    ArrayList<items> itemList;
    ArrayList<items> itemList2;
    //Context applicationContext = MainActivity.getContextOfApplication();
    View myView;
    private Button submitLostBtn;
    private Button foundItemBtn;
    private EditText theFilterEditText;
    private DatabaseReference dbLostReference,mDatabase;
    private itemAdapter adapter;
    private ImageView clearImageView;

    //FOR CHECKBOX SORT
    private AppCompatCheckBox foundCheckbox,lostCheckbox;
    private String foundCheckStatus, lostCheckStatus,finalCheckStatus;
    //FOR ON ITEM CLICK LIST
    public static final String KEY_ITEM_NAME="item_name";
    public static final String KEY_STATUS="item_status";
    public static final String KEY_DATE = "item_date_time";
    public static final String KEY_LOCATION = "item_location";
    public static final String KEY_DESCRIPTION = "item_description";
    public static final String KEY_POSTER = "item_poster";
    public static final String KEY_IMAGE_ID= "item_image_id";
    public static final String KEY_USER_ID= "item_uid";

    List<CharSequence> charSequences = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.newsfeed_layout, container, false);

        Firebase.setAndroidContext(getActivity());
        ((newsFeedActivity) getActivity())
                .setActionBarTitle("News Feed");

        //get the spinner from the xml.
        BetterSpinner dropdown = myView.findViewById(R.id.sortbySpinner);
        //create a list of items for the spinner.
        String[] items = new String[]{"Item Name", "Item Description","Location", "Name of Poster"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line ,items);

        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(spinnerAdapter);

        foundCheckbox = myView.findViewById(R.id.foundCheckbox);
        lostCheckbox = myView.findViewById(R.id.lostCheckbox);
        lostCheckbox.setChecked(false);
        foundCheckbox.setChecked(false);

        itemListView =  myView.findViewById(R.id.itemListView);
        itemListView.setTextFilterEnabled(true);
        submitLostBtn = myView.findViewById(R.id.submitFoundBtn);
        foundItemBtn = myView.findViewById(R.id.foundItemBtn);
        theFilterEditText = myView.findViewById(R.id.theFilterEditText);
        clearImageView = myView.findViewById(R.id.clearImageView);


        itemList = new ArrayList<>();
        //initialize firebase dn
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://lostandfoundfinal.firebaseio.com/");
        dbLostReference= mDatabase.child("items");




        // Query DescendingDateQuery = dbLostReference.orderByChild("dateSubmitted"); //Orders items by Descending date
        Query ApprovedQuery = dbLostReference.orderByChild("approvalStatus").equalTo(1); //Only displays approved items 1

        ApprovedQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //gets all children
                itemList.clear(); //clear listview before populate
                finalCheckStatus ="";
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()){
                    items Items = itemSnapshot.getValue(items.class);
                    itemList.add(0,Items);//remove 0 if ma guba
                    
                }
                if(getActivity()!=null) {

                    adapter = new itemAdapter(getActivity(), itemList);
                    //CHECKBOX ON CHANGE LSITENER
                    lostCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                String entered =  theFilterEditText.getText().toString();
                                theFilterEditText.setText("");
                                theFilterEditText.setText(entered);
                                Log.i("FOUND CHECKBOX: ", "checked");
                                lostCheckStatus = "lostChecked";
                                //sets found checkbox value
                                if(foundCheckbox.isChecked()){
                                    foundCheckStatus = "foundChecked";

                                }else{
                                    foundCheckStatus ="foundUnchecked";
                                }
                                //end set start comapre
                                if(  lostCheckStatus.equals("lostChecked") &&  foundCheckStatus.equals("foundChecked")){
                                    adapter.notifyDataSetChanged();
                                    setFinalCheckStatus("showAll");
                                }else if (foundCheckStatus.equals("foundChecked") && lostCheckStatus.equals("lostUnchecked")){
                                    finalCheckStatus = "showFound";
                                    adapter.getFilter().filter(finalCheckStatus);
                                    setFinalCheckStatus(finalCheckStatus);

                                }else if (foundCheckStatus.equals("foundUnchecked") && lostCheckStatus.equals("lostChecked")){
                                    finalCheckStatus = "showLost";
                                    adapter.getFilter().filter(finalCheckStatus);
                                    setFinalCheckStatus(finalCheckStatus);
                                }else if (foundCheckStatus.equals("foundUnchecked") && lostCheckStatus.equals("lostUnchecked")){
                                    adapter.notifyDataSetChanged();
                                    setFinalCheckStatus("showAll");
                                }

                            }else{
                                String entered =  theFilterEditText.getText().toString();
                                theFilterEditText.setText("");
                                theFilterEditText.setText(entered);
                                Log.i("FOUND CHECKBOX: ", "unchecked");
                                lostCheckStatus = "lostUnchecked";
                                //set value of lost checkbox
                                if(foundCheckbox.isChecked()){
                                    foundCheckStatus = "foundChecked";
                                }else{
                                    foundCheckStatus ="foundUnchecked";
                                }
                                //end set start comapre
                                if( lostCheckStatus.equals("lostChecked") && foundCheckStatus.equals("foundChecked")){
                                    adapter.notifyDataSetChanged();
                                    setFinalCheckStatus("showAll");

                                }else if (foundCheckStatus.equals("foundChecked") && lostCheckStatus.equals("lostUnchecked")){
                                    finalCheckStatus = "showFound";
                                    adapter.getFilter().filter(finalCheckStatus);
                                    setFinalCheckStatus(finalCheckStatus);
                                }else if (foundCheckStatus.equals("foundUnchecked") && lostCheckStatus.equals("lostChecked")){
                                    finalCheckStatus = "showLost";
                                    adapter.getFilter().filter(finalCheckStatus);
                                    setFinalCheckStatus(finalCheckStatus);
                                }else if (foundCheckStatus.equals("foundUnchecked") && lostCheckStatus.equals("lostUnchecked")){
                                    adapter.notifyDataSetChanged();
                                    setFinalCheckStatus("showAll");

                                }


                            }

                        }

                    });
                    foundCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                String entered =  theFilterEditText.getText().toString();
                                theFilterEditText.setText("");
                                theFilterEditText.setText(entered);
                                Log.i("FOUND CHECKBOX: ", "checked");
                                foundCheckStatus = "foundChecked";
                                //sets found checkbox value
                                if(lostCheckbox.isChecked()){
                                    lostCheckStatus = "lostChecked";
                                }else{
                                    lostCheckStatus ="lostUnchecked";
                                }
                                //end set start comapre
                                if(  lostCheckStatus.equals("lostChecked") &&  foundCheckStatus.equals("foundChecked")){
                                    adapter.notifyDataSetChanged();
                                    setFinalCheckStatus("showAll");
                                }else if (foundCheckStatus.equals("foundChecked") && lostCheckStatus.equals("lostUnchecked")){
                                    finalCheckStatus = "showFound";
                                    adapter.getFilter().filter(finalCheckStatus);
                                    setFinalCheckStatus(finalCheckStatus);

                                }else if (foundCheckStatus.equals("foundUnchecked") && lostCheckStatus.equals("lostChecked")){
                                    finalCheckStatus = "showLost";
                                    adapter.getFilter().filter(finalCheckStatus);
                                    setFinalCheckStatus(finalCheckStatus);
                                }else if (foundCheckStatus.equals("foundUnchecked") && lostCheckStatus.equals("lostUnchecked")){
                                    adapter.notifyDataSetChanged();
                                    setFinalCheckStatus("showAll");
                                }

                            }else{
                                String entered =  theFilterEditText.getText().toString();
                                theFilterEditText.setText("");
                                theFilterEditText.setText(entered); //DONOT REMOV
                                Log.i("FOUND CHECKBOX: ", "unchecked");
                                foundCheckStatus = "foundUnchecked";
                                //set value of lost checkbox
                                if(lostCheckbox.isChecked()){
                                    lostCheckStatus = "lostChecked";
                                }else{
                                    lostCheckStatus ="lostUnchecked";
                                }
                                //end set start comapre
                                if( lostCheckStatus.equals("lostChecked") && foundCheckStatus.equals("foundChecked")){
                                    adapter.notifyDataSetChanged();
                                    setFinalCheckStatus("showAll");
                                }else if (foundCheckStatus.equals("foundChecked") && lostCheckStatus.equals("lostUnchecked")){
                                    finalCheckStatus = "showFound";
                                    adapter.getFilter().filter(finalCheckStatus);
                                    setFinalCheckStatus(finalCheckStatus);
                                }else if (foundCheckStatus.equals("foundUnchecked") && lostCheckStatus.equals("lostChecked")){
                                    finalCheckStatus = "showLost";
                                    adapter.getFilter().filter(finalCheckStatus);
                                    setFinalCheckStatus(finalCheckStatus);
                                }else if (foundCheckStatus.equals("foundUnchecked") && lostCheckStatus.equals("lostUnchecked")){
                                    adapter.notifyDataSetChanged();
                                    setFinalCheckStatus("showAll");
                                }


                            }


                        }

                    });
                   // Collections.reverse(itemList); //to order by descending
                    itemListView.setAdapter(adapter);
                    //FILTER EDIT TEXT
                    String filterText = theFilterEditText.getText().toString();
                    /*if(theFilterEditText.getText().toString().equals("") || theFilterEditText.getText().toString().isEmpty() || theFilterEditText.getText().toString().equals(null)){
                        if(!getFinalCheckStatus().equals("showAll")) {
                            adapter.notifyDataSetChanged();
                        }
                        if(getFinalCheckStatus().equals("showFound")) {
                            adapter.getFilter().filter("showFound");
                            //adapter.notifyDataSetChanged();
                        }else if(getFinalCheckStatus().equals("showLost")) {

                            adapter.getFilter().filter("showLost");
                            //adapter.notifyDataSetChanged();

                        }
                    }*/
                    clearImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String entered =  theFilterEditText.getText().toString();
                            theFilterEditText.setText("");
                            if(!getFinalCheckStatus().equals("showAll")) {
                                adapter.notifyDataSetChanged();
                            }
                            if(getFinalCheckStatus().equals("showFound")) {

                                adapter.getFilter().filter(getFinalCheckStatus());
                            }else if(getFinalCheckStatus().equals("showLost")) {
                                adapter.getFilter().filter(getFinalCheckStatus());
                            }
                        }
                    });

                    theFilterEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    adapter.getFilter().filter(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });


                    itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //items item =(items)itemList.get(position);
                            items item = adapter.getItem(position);

                        //items item =((items) parent.getAdapter().getItem(position));
                            //items item =  (items) parent.getItemAtPosition(position);
                            Intent intent = new Intent(getActivity(),onItemClickActivity.class);
                            intent.putExtra(KEY_ITEM_NAME,item.getitemName());
                            intent.putExtra(KEY_STATUS,item.getStatus());
                            intent.putExtra(KEY_DATE,item.getdateSubmitted());
                            intent.putExtra(KEY_LOCATION,item.getlocationDescription());
                            intent.putExtra(KEY_DESCRIPTION,item.getDescription());
                            intent.putExtra(KEY_POSTER,item.getPoster());
                            intent.putExtra(KEY_IMAGE_ID,item.getImageID());
                            intent.putExtra(KEY_USER_ID,item.getUid());
                            intent.putExtra("item_id",item.getItemID());
                            intent.putExtra("item_latitude", item.getLatitude());
                            intent.putExtra("item_longitude", item.getLongitude());

                            if(item.getStatus().equals("Lost")) {
                                intent.putExtra("visibility", "newsLost"); //this extra to to set the setToFoundTextView to Visible
                            }else if(item.getStatus().equals("Found")) {
                                intent.putExtra("visibility", "newsFound"); //this extra to to set the setToFoundTextView to Visible
                            }
                            startActivity(intent);
                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        itemListView.post(new Runnable() {
            @Override
            public void run() {
                itemListView.smoothScrollToPosition(0);
            }
        });

        submitLostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getActivity(),submitLostItemActivity.class);
                startActivity(startIntent);
            }
        });
        foundItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getActivity(),submitFoundItemActivity.class);
                startActivity(startIntent);
            }
        });




        return myView;
    }

    public void setFinalCheckStatus(String finalCheckStatus) {
        this.finalCheckStatus = finalCheckStatus;
        Log.i("final Check Stat: ", finalCheckStatus);
    }

    public String getFinalCheckStatus() {
        return finalCheckStatus;
    }

}//END

/*NOTES Match 25,2018*/
