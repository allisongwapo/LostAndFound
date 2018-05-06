package noelanthony.com.lostandfoundfinal.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import noelanthony.com.lostandfoundfinal.LoginRegister.MainActivity;
import noelanthony.com.lostandfoundfinal.NewsFeed.items;
import noelanthony.com.lostandfoundfinal.R;

public class adminApprove extends AppCompatActivity {

    ListView checkableItemListView;
    List<items> itemApproveList;
    List<String> selectedApprove;
    private Button approveButton;
    private Button declineButton;
    private DatabaseReference dbReference,mDatabase;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_approve);
        declineButton = findViewById(R.id.declineButton);
        approveButton = findViewById(R.id.approveButton);
        final List<items> itemApproveList = new ArrayList<>();
        final List<String> selectedApprove = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://lostandfoundfinal.firebaseio.com/");
        dbReference= mDatabase.child("items");
        final ListView checkableItemListView = findViewById(R.id.checkableItemListView);
        Query notApprovedQuery = dbReference.orderByChild("approvalStatus").equalTo(0);

        notApprovedQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                //gets all children
                itemApproveList.clear(); //clear listview before populate

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()){
                    items Items = itemSnapshot.getValue(items.class);
                    itemApproveList.add(Items);
                }
                if(getApplication()!=null) {
                    final CustomAdapter adapter = new CustomAdapter(adminApprove.this, itemApproveList);
                    checkableItemListView.setAdapter(adapter);
                    checkableItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            final items item = itemApproveList.get(position);
                            if( item.isSelected()){
                                item.setSelected(false);
                                selectedApprove.add(item.getItemID());
                            }
                            else{ item.setSelected(true);
                            selectedApprove.add(item.getItemID());

                            }
                            itemApproveList.set(position,item);
                            adapter.updateRecords(itemApproveList);
                            approveButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    for (int i = 0; i < selectedApprove.size(); i++) {
                                        dbReference.child(selectedApprove.get(i)).child("approvalStatus").setValue(1);
                                    }

                                    Toast.makeText(getApplicationContext(), "Items approved",Toast.LENGTH_SHORT).show();
                                }
                            });
                            declineButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    for (int i = 0; i < selectedApprove.size(); i++) {
                                            /*
                                            //makes a declinedItems table and deletes selected item in items table
                                            DatabaseReference decDatabase = mDatabase.child("declinedItems");
                                            DatabaseReference declinedItem = decDatabase.push();
                                            items Item = new items();
                                            declinedItem.child("itemName").setValue((dataSnapshot.getValue(items.class).getitemName()));
                                            declinedItem.child("lastSeenLocation").setValue(dataSnapshot.getValue(items.class).getlastSeenLocation());
                                            declinedItem.child("description").setValue(Item.getDescription());
                                            declinedItem.child("poster").setValue(Item.getPoster());
                                            declinedItem.child("dateSubmitted").setValue(Item.getdateSubmitted());
                                            declinedItem.child("status").setValue(Item.getStatus());
                                            declinedItem.child("uid").setValue(Item.getUid()); //for mySubmissions Filter
                                            declinedItem.child("approvalStatus").setValue(2);
                                            declinedItem.child("itemID").setValue(Item.getItemID());
                                            */
                                        /*if(dataSnapshot.child(selectedApprove.get(i)).getValue()!= null) {
                                           dbReference.child(selectedApprove.get(i)).removeValue();
                                        }FOR ITEM REMOVAL*/
                                        dbReference.child(selectedApprove.get(i)).child("approvalStatus").setValue(2);
                                    }

                                    Toast.makeText(getApplicationContext(), "Items declined",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    });
                    Collections.reverse(itemApproveList); //to order by descending
                    checkableItemListView.setAdapter(adapter);
                }
            }


            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
    @Override
    public void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }


}
