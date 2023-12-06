// *** Author: Daniel Coffland ***

package csustan.edu.cs4800.warriordiningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import csustan.edu.cs4800.warriordiningapp.databinding.ActivityMainBinding;

// *** Author: Daniel Coffland ***

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView username = (TextView) findViewById(R.id.usernameText);
        Button loginButton = (Button) findViewById(R.id.loginButton);

        Log.d("pre-data passing", "onCreate: " + username);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(username.getText().toString().equals(""))) {
                    // username is filled with something
                    // making edit text go to a string value
                    String s_username = username.getText().toString();
                    Toast.makeText(MainActivity.this, "Logging In...", Toast.LENGTH_SHORT).show();
                    // creating an intent and using it to package when changing to menu
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    // making a bundle to put into extras of intent
                    Bundle bundle = new Bundle();
                    bundle.putString("username", s_username);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    // not filled, aka do essentially nothing but feedback would be nice
                    Toast.makeText(MainActivity.this, "Please Input Your Username", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}