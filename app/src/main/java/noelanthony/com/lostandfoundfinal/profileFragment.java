package noelanthony.com.lostandfoundfinal;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

/**
 * Created by Noel on 16/02/2018.
 */

public class profileFragment extends Fragment implements View.OnClickListener {


    private static final int CHOOSE_IMAGE = 101;
    View myView;
    ImageView uploadImageView;
    TextView nameTextView, updateinfoTextView, idnoTextView, datejoinedTextView, emailverifyTextView, itemsreturnedTextView;
    Uri uriProfileImage;
    Context applicationContext = MainActivity.getContextOfApplication();
    ProgressBar progressBar;
    String profileImageURL;
    Button saveBtn;
    EditText nameEditText;


    //FIREBASE Stuff
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CHOOSE_IMAGE && resultCode ==  Activity.RESULT_OK && data != null && data.getData()!=null){
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), uriProfileImage);
                uploadImageView.setImageBitmap(bitmap);
                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage(){
        StorageReference profileImageReference = FirebaseStorage.getInstance().getReference("profileimage/"+System.currentTimeMillis()+".jpg");
        if (uriProfileImage != null){
            progressBar.setVisibility(View.VISIBLE);
            profileImageReference.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    profileImageURL = taskSnapshot.getDownloadUrl().toString();

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(applicationContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    //change applicationContext if mu error //profileFragment.this
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profile_layout,container,false);
        Firebase.setAndroidContext(applicationContext);


        uploadImageView = (ImageView)myView.findViewById(R.id.uploadImageView);
        nameTextView = (TextView)myView.findViewById(R.id.nameTextView);
        updateinfoTextView = (TextView)myView.findViewById(R.id.updateinfoTextView);
        idnoTextView = (TextView)myView.findViewById(R.id.idnoTextView);
        datejoinedTextView=(TextView)myView.findViewById(R.id.datejoinedTextView);
        progressBar=(ProgressBar)myView.findViewById(R.id.progressbar);
        saveBtn = (Button)myView.findViewById(R.id.saveBtn);
        nameEditText=(EditText)myView.findViewById(R.id.nameEditText);
        emailverifyTextView = (TextView)myView.findViewById(R.id.emailverifyTextView);
        itemsreturnedTextView = (TextView)myView.findViewById(R.id.itemsreturnedTextView);



        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user !=null){
                    //user is signed in
                    Toast.makeText(applicationContext, user.getEmail()+" is signed in", Toast.LENGTH_SHORT ).show();
                } else{
                    //user is signed out
                    Toast.makeText(applicationContext, "Successfully logged out", Toast.LENGTH_SHORT ).show();
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        uploadImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showImageChooser();
                saveBtn.setVisibility(View.VISIBLE);
            }
        });

        loadUserInformation();

        myView.findViewById(R.id.saveBtn).setOnClickListener(this);
        myView.findViewById(R.id.updateinfoTextView).setOnClickListener(this);

        return myView;
    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds:dataSnapshot.getChildren()){
            UserInformation uInfo = new UserInformation();
            uInfo.setName(ds.child(userID).getValue(UserInformation.class).getName());//sets name
            //uInfo.setEmail(ds.child(userID).getValue(UserInformation.class).getEmail());//sets email
            uInfo.setDatejoined(ds.child(userID).getValue(UserInformation.class).getDatejoined());//sets name
            uInfo.setItemsreturned(ds.child(userID).getValue(UserInformation.class).getItemsreturned());
            uInfo.setIdnumber(ds.child(userID).getValue(UserInformation.class).getIdnumber());

            nameTextView.setText(uInfo.getName());
            datejoinedTextView.setText(uInfo.getDatejoined());
            itemsreturnedTextView.setText(String.valueOf(uInfo.getItemsreturned()));
            idnoTextView.setText(uInfo.getIdnumber());
        }
    }


    //onstart
    @Override
    public void onResume() {
        super.onResume();

        //logs user out if not logged in
        if(mAuth.getCurrentUser() == null) {
            //getActivity().getFragmentManager().popBackStack();
            getActivity().onBackPressed(); //finish activity and go to login
            startActivity(new Intent(applicationContext, MainActivity.class));
        }
    }

    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null) {

            if (user.getPhotoUrl() != null) {
                Glide.with(this).load(user.getPhotoUrl().toString()).into(uploadImageView);
            }
            if (user.getDisplayName() != null) {
                nameEditText.setText(user.getDisplayName());
            }
            if(user.isEmailVerified()){
                emailverifyTextView.setText("Email Verified");
            }else{
                emailverifyTextView.setText("Email not Verified. Click to verify");
                emailverifyTextView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(applicationContext, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                                emailverifyTextView.setText("Verification Email sent. Click to resend");
                            }
                        });
                    }
                });
            }
        }
    }

    private void saveUserInformation() {
            String displayName = nameEditText.getText().toString();
            nameTextView.setText(displayName);

            if (displayName.isEmpty()){
                nameEditText.setError("Please enter your name");
                nameEditText.requestFocus();
                return;
            }
        FirebaseUser user =mAuth.getCurrentUser();

            if(user!=null && profileImageURL !=null){
                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(displayName).setPhotoUri(Uri.parse(profileImageURL)).build();

                user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(applicationContext, "Profile Updated", Toast.LENGTH_SHORT ).show();
                        }
                    }
                });
            }
    }

    private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateinfoTextView:
                updateinfoTextView.setVisibility(View.INVISIBLE);
                nameTextView.setVisibility(View.INVISIBLE);
                nameEditText.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.VISIBLE);
                break;

            case R.id.saveBtn:
                nameTextView.setVisibility(View.VISIBLE);
                nameEditText.setVisibility(View.INVISIBLE);
                saveBtn.setVisibility(View.INVISIBLE);
                updateinfoTextView.setVisibility(View.VISIBLE);
                saveUserInformation();
                break;


        }
    }
}//END