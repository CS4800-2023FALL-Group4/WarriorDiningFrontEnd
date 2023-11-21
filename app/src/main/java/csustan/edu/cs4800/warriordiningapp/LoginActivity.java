package csustan.edu.cs4800.warriordiningapp;


import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.text.TextUtils;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;


public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;


    private Button buttonLogin;
    private FirebaseAuth mAuth;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();
        buttonLogin = findViewById(R.id.buttonLogin);

        emailEditText = findViewById(R.id.emailEditText);


        buttonLogin .setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Call a method to perform login
                    performLogin();
                }
            });
        }

        private void performLogin(){
    String email = emailEditText.getText().toString().trim();
    // Check if the email is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        return;
    }


            }





            private void showEmailSentMessage() {
        Toast.makeText(this, "Email sent. Check your email to complete the login process.", Toast.LENGTH_SHORT).show();
        //  Navigate to the next activity or perform other actions
    }
    private void handleLoginError(Exception exception) {
        // Handle specific error cases
        if (exception instanceof FirebaseAuthInvalidUserException) {
            // The user does not exist, show appropriate message
            Toast.makeText(this, "User does not exist. Please sign up.", Toast.LENGTH_SHORT).show();
        } else if (exception instanceof FirebaseAuthException) {
            // Other FirebaseAuthException, show a generic error message
            Toast.makeText(this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
        // Handle other specific error cases if needed
    }


    private void sendSignInLink(String Email) {
    ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
            // URL you want to redirect back to. The domain (www.example.com) for this
            // URL must be whitelisted in the Firebase Console.
            .setUrl("https://warriordiningapp.page.link/warrior1")
            // This must be true
            .setHandleCodeInApp(true)
            .setAndroidPackageName(
                    "com.example.android",
                    true, /* installIfNotAvailable */
                    "12"    /* minimumVersion */)
            .build();

    FirebaseAuth auth = FirebaseAuth.getInstance();
    auth.sendSignInLinkToEmail(Email, actionCodeSettings)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Email link sent. Check your email.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Error sending email link", Toast.LENGTH_SHORT).show();
                    }
                }
            });
}

    private void handleEmailVerification() {
        Intent intent = getIntent();
        Uri emailLink = intent.getData();

        if (emailLink != null && mAuth.isSignInWithEmailLink(emailLink.toString())) {
            String email = "Someemail@domain.com";

            mAuth.signInWithEmailLink(email, emailLink.toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Successfully signed in with email link!");
                                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                            } else {
                                Log.e(TAG, "Error signing in with email link", task.getException());
                                Toast.makeText(LoginActivity.this, "Error signing in with email link", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    } }











