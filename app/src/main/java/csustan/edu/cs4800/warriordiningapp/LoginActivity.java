///* package csustan.edu.cs4800.warriordiningapp;


import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.text.TextUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button sendLinkButton;
    private FirebaseAuth mAuth;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        sendLinkButton = findViewById(R.id.sendLinkButton);


        sendLinkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
            String email = emailEditText.getText().toString().trim();

       if (!email.isEmpty()){
           sendSignInLink(email);
            } else {
                Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            }
        }
    });

        // check if the app was open from dynamic link


}
private void sendSignInLink(String Email) {
    ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
            // URL you want to redirect back to. The domain (www.example.com) for this
            // URL must be whitelisted in the Firebase Console.
            .setUrl("https://www.example.com/finishSignUp?cartId=1234")
            // This must be true
            .setHandleCodeInApp(true)
            .setIOSBundleId("com.example.ios")
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
    } } *////











