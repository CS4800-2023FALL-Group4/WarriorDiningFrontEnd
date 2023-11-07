package csustan.edu.cs4800.warriordiningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;

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
import java.util.ArrayList;

import csustan.edu.cs4800.warriordiningapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;    // gotta bind the fragment
    ArrayList<String> menuList;     // ArrayList for menu later
    ArrayAdapter<String> menuAdapter;
    Handler menuHandler = new Handler();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeMenu();
        new fetchMenu().start();
        
//        binding.fetchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new fetchMenu().start();
//            }
//        });
    }

    private void initializeMenu() {

        menuList = new ArrayList<>();
        menuAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,menuList);
        binding.menuList.setAdapter(menuAdapter);

    }

    class fetchMenu extends Thread {
        String data = "";


        @Override
        public void run() {


            menuHandler.post(new Runnable() {
                @Override
                public void run() {

                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Fetching Menu...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                }
            });

            try {
                URL url = new URL("https://warrior-dining-server.replit.app/menu");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream iStream = httpURLConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream));
                String line;

                while ((line = bReader.readLine()) != null) {
                    data = data + line;
                }

                if (!data.isEmpty()) {
                    JSONObject jObject = new JSONObject(data);
                    JSONArray menu = jObject.getJSONArray("menus");
                    menuList.clear();

                    for (int i = 0; i < menu.length(); i++) {
                        JSONObject menuItems = menu.getJSONObject(i);
                        String menuItem = menuItems.getString("foods");
                        menuList.add(menuItem);
                    }
                }

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            menuHandler.post(new Runnable() {
                @Override
                public void run() {

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    menuAdapter.notifyDataSetChanged();

                }
            });

        }
    }

}