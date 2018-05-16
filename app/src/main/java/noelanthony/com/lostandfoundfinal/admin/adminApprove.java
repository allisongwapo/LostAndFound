package noelanthony.com.lostandfoundfinal.admin;

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

import com.google.android.gms.tasks.OnSuccessListener;
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

import noelanthony.com.lostandfoundfinal.R;
import noelanthony.com.lostandfoundfinal.loginregister.MainActivity;
import noelanthony.com.lostandfoundfinal.newsfeed.items;

public class adminApprove extends AppCompatActivity {

    ListView checkableItemListView;
    List<items> itemApproveList;
    List<String> selectedApprove;
    private Button approveButton;
    private Button declineButton;
    private DatabaseReference dbReference,mDatabase;
    FirebaseAuth mAuth;


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
                String tokenRemove ="";
                String current_id = mAuth.getCurrentUser().getUid();
                DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference().child("users").child(current_id).child("token_id");//to send admin notification
                adminRef.setValue(tokenRemove).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        Intent backtologin = new Intent(adminApprove.this,MainActivity.class);
                        startActivity(backtologin);
                    }
                });
                //FirebaseAuth.getInstance().signOut();
                //finish();
                //startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_approve);

        //FOR ADMIN NOTIFICATION
        mAuth = FirebaseAuth.getInstance();
       // FirebaseMessaging.getInstance().subscribeToTopic("ADMIN");
        declineButton = findViewById(R.id.declineButton);
        approveButton = findViewById(R.id.approveButton);
        final List<items> itemApproveList = new ArrayList<>();
        final List<String> selectedApprove = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://lostandfoundfinal.firebaseio.com/");
        dbReference= mDatabase.child("items");
        final ListView checkableItemListView = findViewById(R.id.checkableItemListView);
        Query notApprovedQuery = dbReference.orderByChild("approvalStatus").equalTo(0);
        String dataMessage = getIntent().getStringExtra("message");
        String dataFrom = getIntent().getStringExtra("from_user_id");

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
                                selectedApprove.remove(item.getItemID());
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
                                    if (selectedApprove.isEmpty()) {
                                        Toast.makeText(getApplicationContext(), "Please select an item", Toast.LENGTH_SHORT).show();
                                    } else { Toast.makeText(getApplicationContext(), "Item/s approved", Toast.LENGTH_SHORT).show(); }
                                    selectedApprove.clear();


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
                                            declinedItem.child("itemName").setValue((dataSnapshot.getValue(items.class).getitemName()));*/
                                        dbReference.child(selectedApprove.get(i)).child("approvalStatus").setValue(2);
                                    }
                                    if (selectedApprove.isEmpty()) {
                                        Toast.makeText(getApplicationContext(), "Please select an item", Toast.LENGTH_SHORT).show();

                                    } else { Toast.makeText(getApplicationContext(), "Item/s declined", Toast.LENGTH_SHORT).show(); }
                                    selectedApprove.clear();

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
        /*
        0 - PENDING
        1 - APPROVED
        2 - DECLINED
         */


    }



}//END