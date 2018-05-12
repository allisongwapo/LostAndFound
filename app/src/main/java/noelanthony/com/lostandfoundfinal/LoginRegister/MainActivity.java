package noelanthony.com.lostandfoundfinal.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import noelanthony.com.lostandfoundfinal.Admin.adminApprove;
import noelanthony.com.lostandfoundfinal.NavMenu.newsFeedActivity;
import noelanthony.com.lostandfoundfinal.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    FirebaseAuth mAuth;
    EditText emailEditText, passwordEditText;
    Button loginButton;
    TextView registerTextView;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contextOfApplication = getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);
        progressbar = findViewById(R.id.progressbar);

        findViewById(R.id.loginButton).setOnClickListener(this);
        findViewById(R.id.registerTextView).setOnClickListener(this);


    }

    private void userLogin() {

        final String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Please enter your email address");
            emailEditText.requestFocus();
            progressbar.setVisibility(View.INVISIBLE);
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email");
            emailEditText.requestFocus();
            progressbar.setVisibility(View.INVISIBLE);
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            progressbar.setVisibility(View.INVISIBLE);
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password is more than 6 characters");
            passwordEditText.requestFocus();
            progressbar.setVisibility(View.INVISIBLE);
            return;
        }
        progressbar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(email.equals("admin@admin.com")){
                        finish();
                        Intent startIntent = new Intent(getApplicationContext(), adminApprove.class);
                        startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(startIntent);
                    }
                    else{
                        finish();
                        Intent startIntent = new Intent(getApplicationContext(), newsFeedActivity.class);
                        startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(startIntent);
                    }
                }else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    progressbar.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, newsFeedActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerTextView:
                finish();
                startActivity(new Intent(this, registerActivity.class));
                break;

            case R.id.loginButton:
                userLogin();
                break;
        }
    }
}
