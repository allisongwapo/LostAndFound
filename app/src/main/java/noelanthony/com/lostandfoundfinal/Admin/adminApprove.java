package noelanthony.com.lostandfoundfinal.Admin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_approve);

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

}
