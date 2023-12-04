 package csustan.edu.cs4800.warriordiningapp;



import static com.google.firebase.FirebaseApp.*;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Button;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import androidx.annotation.NonNull;

public class SignInActivity<FirebaseUser> extends SharedPreferencesActivity implements View.OnClickListener {
    private static String TAG = SignInActivity.class.getCanonicalName();
    private FirebaseAuth firebaseAuth;

    private static String DEEP_LINK = "https://warriordiningapp.page.link/warrior1\n";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_sign_in); // Change to the correct layout file
        Button b = findViewById(R.id.button);
        b.setOnClickListener(this);
        FirebaseApp.initializeApp(this);
        initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        callSetUpDynamicLink();
    }


    private void callSetUpDynamicLink() {
        Log.d(TAG, "callSetUpDynamicLink");
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Log.d(TAG, "callGetDynamicLink, success");
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }
                        Log.d(TAG, "callGetDynamicLink, deepLink " + deepLink);

                        handleSuccess(deepLink);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "getDynamicLink:onFailure", e);
                                handleError(e.getMessage());
                            }
                        }
                );
    }

    private void switchToMainActivity() {
        Log.d(TAG, "switchToMainActivity");
        Intent i = new Intent(this, MainActivity.class);
        this.startActivity(i);
    }

    private void handleError(String errorMessage) {
        Log.d(TAG, "handleError");

        setTextOnError(errorMessage);
    }

    private void handleSuccess(Uri deepLink) {
        Log.d(TAG, "handleSuccess");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        if (intent == null || intent.getData() == null) return;
        String emailLink = intent.getData().toString();

        String email = getTemporarilySavedEmail();
        if (auth.isSignInWithEmailLink(emailLink)) {

            auth.signInWithEmailLink(email, emailLink)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign-in successful
                                FirebaseUser user = (FirebaseUser) task.getResult().getUser();
                                Log.d(TAG, "Successfully signed in with email link!");
                                AuthResult result = task.getResult();

                                switchToMainActivity();
                            } else {
                                handleError("Could not sign in");
                                Log.e(TAG, "Error signing in with email link", task.getException());
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick");
        EditText et = findViewById(R.id.editText);
        final String email = et.getText().toString();
        temporarilySaveEmail(email);
        Log.d(TAG, "onClick email? " + email);
        Log.d(TAG, "A new message to make sure recompiled");
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()

                .setUrl("https://warriordiningapp.page.link/warrior1")
                .setHandleCodeInApp(true)
                .setAndroidPackageName(
                        "csustan.edu.cs4800.warriordiningapp",
                        true, /* installIfNotAvailable */
                        "1"    /* minimumVersion */)
                .setDynamicLinkDomain("warriordiningapp.page.link")
                .build();

        firebaseAuth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "email? " + email);

                        boolean emailWasSent = task.isSuccessful();
                        if (emailWasSent) {
                            Log.d(TAG, "Email sent successfully.");
                        } else {
                            Exception e = task.getException();
                            Log.e(TAG, "Error sending email link: " + e.getMessage(), e);
                        }
                        setText(emailWasSent);
                    }
                });

    }

    protected void setText(boolean success) {
        Log.d(TAG, "setText");
        TextView tv = findViewById(R.id.tvEmailResults);
        if (success) {
            tv.setText(getText(R.string.success_email_sent));
        } else {
            tv.setText(getText(R.string.not_success_email_sent));
        }
    }



    protected void setTextOnError(String errorMessage) {
        Log.d(TAG, "setImageOnError");
        TextView tv = findViewById(R.id.tvEmailResults);
        tv.setText(errorMessage);
    }

}