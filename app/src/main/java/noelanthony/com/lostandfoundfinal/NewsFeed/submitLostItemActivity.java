package noelanthony.com.lostandfoundfinal.NewsFeed;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

import noelanthony.com.lostandfoundfinal.NavMenu.newsFeedActivity;
import noelanthony.com.lostandfoundfinal.Profile.UserInformation;
import noelanthony.com.lostandfoundfinal.R;

public class submitLostItemActivity extends AppCompatActivity {

    private EditText itemnameEditText;
    private EditText lastseenEditText;
    private EditText descriptionEditText;
    private ImageButton uploadImageButton;
    private Button submitLostButton;
    private ProgressBar progressBar;
    private DatabaseReference mDatabase,nameRef;
    private FirebaseAuth mAuth;
    private String userID, poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_lost_item);

        itemnameEditText = findViewById(R.id.itemnameEditText);
        lastseenEditText = findViewById(R.id.locationdescEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        submitLostButton = findViewById(R.id.submitFoundBtn);
        progressBar = findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        submitLostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(v);
            }
        });

    }


    public void showAlertDialog(View v){
        final String itemName = itemnameEditText.getText().toString().trim();
        final String lastSeen = lastseenEditText.getText().toString().trim();
        final String description = descriptionEditText.getText().toString().trim();
        final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        //check if name is empty
        if(itemName.isEmpty()){
            itemnameEditText.setError("Item name is required");
            itemnameEditText.requestFocus();
            return;
        }

        if(description.isEmpty()){
            descriptionEditText.setError("Description is required");
            descriptionEditText.requestFocus();
            return;
        }
        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setTitle("Confirm");
        alert.setMessage("Item Name: " + itemnameEditText.getText() + "\n Last Seen in: " + lastseenEditText.getText() + "\n Description: " + descriptionEditText.getText());
        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("items").child("lostItems");
                DatabaseReference nameRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
                final DatabaseReference item = mDatabase.child(mAuth.getCurrentUser().getUid() + "Lost" + itemName);
                item.child("itemName").setValue(itemName);
                item.child("lastSeenLocation").setValue(lastSeen);
                item.child("description").setValue(description);
                nameRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        showData(dataSnapshot);
                        item.child("poster").setValue(poster);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                item.child("dateSubmitted").setValue(currentDateTimeString);
                item.child("status").setValue("Lost");
                Toast.makeText(getApplicationContext(), "Submission Successful", Toast.LENGTH_SHORT).show();
                Intent startIntent = new Intent(getApplicationContext(),newsFeedActivity.class);
                startActivity(startIntent);

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.create().show();
    }
    private void showData(DataSnapshot dataSnapshot) {
        UserInformation uInfo = new UserInformation();
        uInfo.setName(dataSnapshot.getValue(UserInformation.class).getName());//sets name
        poster = uInfo.getName();

    }

} // END
