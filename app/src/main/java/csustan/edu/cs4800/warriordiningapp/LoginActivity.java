//* package csustan.edu.cs4800.warriordiningapp;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button sendLinkButton;
    private FirebaseAuth mAuth;

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

        if (!email.isEmpty()) {
                sendSignInLin( email);
            } else {
                Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            }
        }
    });

        // check if the app was open from dynamic link
        checkPermission;

}
private void sendSignInLink(string Email){
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
    auth.sendSignInLinkToEmail(email, actionCodeSettings)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent.");
                    }

                }
            }); *///










