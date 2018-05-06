package noelanthony.com.lostandfoundfinal.NewsFeed;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

import noelanthony.com.lostandfoundfinal.LoginRegister.MainActivity;
import noelanthony.com.lostandfoundfinal.Profile.UserInformation;
import noelanthony.com.lostandfoundfinal.R;

public class submitLostItemActivity extends AppCompatActivity {

    private EditText itemnameEditText;
    private EditText lastseenEditText;
    private EditText descriptionEditText;
    private ImageButton uploadImageButton;
    private Button submitLostButton;
    private DatabaseReference mDatabase,nameRef;
    private FirebaseAuth mAuth;
    private String userID, poster;
    private ProgressBar mProgressBar;
    private ImageView displayImageView;
    Context applicationContext = MainActivity.getContextOfApplication();

    //for image storage
    private StorageReference mStorage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_lost_item);

        itemnameEditText = findViewById(R.id.itemnameEditText);
        lastseenEditText = findViewById(R.id.locationdescEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        submitLostButton = findViewById(R.id.submitFoundBtn);
        mProgressBar = findViewById(R.id.progressbar);
        displayImageView = findViewById(R.id.displayImageView);

        mStorageRef = FirebaseStorage.getInstance().getReference("lostItemImage/");
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();



        //UPLOAD IMAGE
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        //SUBMIT USER SUBMISSIONS
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
        //ALERT DIALOG. USER SUBMISSIONS IS SHOWN. USER CHOOSES TO ACCEPT OR DECLINE.
        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setCancelable(true);
        alert.setTitle("Confirm");
        alert.setMessage("Item Name: " + itemnameEditText.getText() + "\n Last Seen in: " + lastseenEditText.getText() + "\n Description: " + descriptionEditText.getText());
        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("items");
                DatabaseReference nameRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
                final DatabaseReference item = mDatabase.push();
                String key = item.getKey();
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
                item.child("uid").setValue(userID); //for mySubmissions Filter
                item.child("approvalStatus").setValue(0);
                item.child("itemID").setValue(key);

                //this block of code prevents multiple image upload
                if(mUploadTask!=null && mUploadTask.isInProgress()){
                    Toast.makeText(applicationContext, "Upload in progress", Toast.LENGTH_SHORT).show();
                }
                else if(mImageUri!=null){
                    StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));
                    mUploadTask = fileReference.putFile(mImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    mProgressBar.setVisibility(View.VISIBLE);
                                    Handler handler = new Handler(); //to delay progress bar by half a second
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mProgressBar.setProgress(0);
                                        }
                                    },500);
                                    Toast.makeText(applicationContext,"Upload successful", Toast.LENGTH_LONG).show();
                                   // items upload = new items (itemnameEditText.getText().toString().trim(),taskSnapshot.getDownloadUrl().toString());
                                   // String uploadId = mDatabaseRef.push().getKey();
                                    item.child("imageID").setValue(taskSnapshot.getDownloadUrl().toString());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(applicationContext, e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    mProgressBar.setProgress((int)progress);
                                }
                            });
                }else{
                    Toast.makeText(applicationContext,"No file selected", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), "Submission Successful", Toast.LENGTH_SHORT).show();
                Intent startIntent = new Intent(getApplicationContext(),newsfeedFragment.class);
                startActivity(startIntent);
                // FragmentManager fragmentManager = getFragmentManager();
                //fragmentManager.beginTransaction().replace(R.id.content_frame, new mySubmissionsFragment()).commit();

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

    //IMAGE FUNCTIONS
    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData() !=null){
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(displayImageView);
        }
    }

} // END
