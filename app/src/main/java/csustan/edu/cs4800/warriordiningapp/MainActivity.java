////* package csustan.edu.cs4800.warriordiningapp;


import static com.google.firebase.FirebaseApp.initializeApp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends SharedPreferencesActivity {
    private final String TAG = MainActivity.class.getCanonicalName();

    private FirebaseAuth firebaseAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        initializeApp(this);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            setUISignedIn(currentUser);
        } else {
            setUISignedOut(null);
        }
    }



    protected void setUISignedIn(FirebaseUser currentUser) {
        Log.d(TAG, "setUISignedIn");

        String foundEmail = currentUser.getEmail();
        Log.d(TAG, "setUISignedIn finds foundEmail to be " + foundEmail);
        if (foundEmail != null && !foundEmail.isEmpty()) {
            TextView tv = findViewById(R.id.tvUsername);
            tv.setText(foundEmail);
        } else{
            TextView tv = findViewById(R.id.tvUsername);
            tv.setText("Not signed in");
        }
    }

    protected void setUISignedOut(String errorMessage) {
        Log.d(TAG, "setUISignedOut");
        if (errorMessage != null) {
            TextView tvIn = findViewById(R.id.tvInstructions);
            tvIn.setVisibility(View.VISIBLE);
            tvIn.setText(errorMessage);
        }
        TextView tv = findViewById(R.id.tvSignedIn);
        tv.setVisibility(View.INVISIBLE);
        String tmpEmail = getTemporarilySavedEmail();
        Log.d(TAG, "Found tmpEmail " + tmpEmail);
        if (!tmpEmail.isEmpty()) {
            Log.d(TAG, "Setting email to tmpEmail");
            EditText et = findViewById(R.id.editText);
            et.setText(tmpEmail);
        }
    }
} *////