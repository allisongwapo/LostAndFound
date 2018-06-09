package noelanthony.com.lostandfoundfinal.profile;

import android.app.Activity;
import android.app.Fragment;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
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

import noelanthony.com.lostandfoundfinal.R;
import noelanthony.com.lostandfoundfinal.loginregister.MainActivity;
import noelanthony.com.lostandfoundfinal.navmenu.newsFeedActivity;

/**
 * Created by Noel on 16/02/2018.
 */

public class profileFragment extends Fragment implements View.OnClickListener {


    private static final int CHOOSE_IMAGE = 101;
    View myView;
    ImageView uploadImageView;
    TextView nameTextView, idnoTextView, datejoinedTextView, emailverifyTextView,emailTextView;// itemsreturnedTextView;
    Uri uriProfileImage;
    //Context applicationContext = MainActivity.getContextOfApplication();
    ProgressBar progressBar;
    String profileImageURL;
    Button saveBtn;
    EditText nameEditText;

    //FIREBASE Stuff
    //private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef,mDatabase;
    private String userID;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CHOOSE_IMAGE && resultCode ==  Activity.RESULT_OK && data != null && data.getData()!=null){
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uriProfileImage);
                uploadImageView.setImageBitmap(bitmap);
                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage(){
        final StorageReference profileImageReference = FirebaseStorage.getInstance().getReference("profileimage/"+System.currentTimeMillis()+".jpg");
        if (uriProfileImage != null){
            progressBar.setVisibility(View.VISIBLE);
            UploadTask uploadTask =  profileImageReference.putFile(uriProfileImage);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return profileImageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        progressBar.setVisibility(View.GONE);
                        profileImageURL = downloadUri.toString();
                        Toast.makeText(getActivity(),"Upload successful", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), "Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

/*
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    //change applicationContext if mu error //profileFragment.this
                }
            })*/
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profile_layout,container,false);
        Firebase.setAndroidContext(getActivity());


        uploadImageView = myView.findViewById(R.id.uploadImageView);
        nameTextView = myView.findViewById(R.id.nameTextView);
        //updateinfoTextView = myView.findViewById(R.id.updateinfoTextView);
        //idnoTextView = myView.findViewById(R.id.idnoTextView);
        datejoinedTextView= myView.findViewById(R.id.datejoinedTextView);
        progressBar= myView.findViewById(R.id.progressbar);
        saveBtn = myView.findViewById(R.id.saveBtn);
        nameEditText= myView.findViewById(R.id.nameEditText);
        emailverifyTextView = myView.findViewById(R.id.emailverifyTextView);
        emailTextView = myView.findViewById(R.id.emailTextView);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://lostandfoundfinal.firebaseio.com/");
        myRef = mDatabase.child("users").child(userID);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user !=null){
                    //user is signed in
                    Toast.makeText(getActivity(), user.getEmail()+" is signed in", Toast.LENGTH_SHORT ).show();
                } else{
                    //user is signed out
                    Toast.makeText(getActivity(), "Successfully logged out", Toast.LENGTH_SHORT ).show();
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
        //myView.findViewById(R.id.updateinfoTextView).setOnClickListener(this);

        return myView;
    }

    private void showData(DataSnapshot dataSnapshot) {
       // for(DataSnapshot ds:dataSnapshot.getChildren()){
        //UserInformation facts = dataSnapshot.getValue(UserInformation.class);

        UserInformation uInfo = new UserInformation();
            uInfo.setName(dataSnapshot.getValue(UserInformation.class).getName());//sets name
            uInfo.setEmail(dataSnapshot.getValue(UserInformation.class).getEmail());//sets email
            uInfo.setDatejoined(dataSnapshot.getValue(UserInformation.class).getDatejoined());//sets name
          //  uInfo.setItemsreturned(dataSnapshot.getValue(UserInformation.class).getItemsreturned());
           // uInfo.setIdnumber(dataSnapshot.getValue(UserInformation.class).getIdnumber());

            nameTextView.setText(uInfo.getName());
            datejoinedTextView.setText("Date Joined: " + uInfo.getDatejoined());
             emailTextView.setText("Email: "+ String.valueOf(uInfo.getEmail()).toString());
            //itemsreturnedTextView.setText("Items Returned: "+ String.valueOf(uInfo.getItemsreturned()).toString());
           // idnoTextView.setText(uInfo.getIdnumber());
        //}
    }

    //onstart
    @Override
    public void onResume() {
        super.onResume();

        //logs user out if not logged in
        if(mAuth.getCurrentUser() == null) {
            //getActivity().getFragmentManager().popBackStack();
            getActivity().onBackPressed(); //finish activity and go to login
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
        ((newsFeedActivity) getActivity())
                .setActionBarTitle("Profile");
    }

    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this).load(user.getPhotoUrl().toString()).into(uploadImageView);
            }
           // if (user.getDisplayName() != null) {
             //   nameEditText.setText(user.getDisplayName());
           // }
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
                                Toast.makeText(getActivity(), "Verification Email Sent", Toast.LENGTH_SHORT).show();
                                emailverifyTextView.setText("Verification Email sent. Click to resend");
                            }
                        });
                    }
                });
            }
        }
    }

    private void saveUserInformation() {
          //  String displayName = nameEditText.getText().toString();
          //  nameTextView.setText(displayName);
           // if (displayName.isEmpty()){
          //      nameEditText.setError("Please enter your name");
          //      nameEditText.requestFocus();
         //       return;
         //   }
        FirebaseUser user =mAuth.getCurrentUser();
            if(user!=null && profileImageURL !=null){
                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(profileImageURL)).build();//.setDisplayName(displayName)
                user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT ).show();
                            myRef.child("image").setValue(profileImageURL);

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
        nameTextView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

           // case R.id.updateinfoTextView:
           //     updateinfoTextView.setVisibility(View.INVISIBLE);
           //     nameTextView.setVisibility(View.INVISIBLE);
           //    nameEditText.setVisibility(View.VISIBLE);
           //     saveBtn.setVisibility(View.VISIBLE);
          //      break;

            case R.id.saveBtn:
                //onameEditText.setVisibility(View.GONE);
                saveBtn.setVisibility(View.INVISIBLE);
                 progressBar.setVisibility(View.GONE);
                //updateinfoTextView.setVisibility(View.VISIBLE);
                nameTextView.setVisibility(View.VISIBLE);
                saveUserInformation();
                break;
        }
    }
}//END