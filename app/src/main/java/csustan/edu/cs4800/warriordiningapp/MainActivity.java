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
import java.util.Arrays;

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
        // sets the view to main activity
        setContentView(binding.getRoot());

        initializeMenu();
        // onload menu fetch
        // probably will change to add methods for each menu type
        // (i.e. fetchBreakfast(), fetchLunch(), fetchDinner()
        new fetchMenu().start();

        binding.fetchBreakfastMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override   // haven't found an onload feature yet, will do next
            public void onClick(View v) {
                new fetchBreakfastMenu().start();
            }
        });
    }

    private void initializeMenu() {
        // making menu array and adapter to populate the listview
        menuList = new ArrayList<>();
        menuAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,menuList);
        binding.menuList.setAdapter(menuAdapter);

    }

    // make separate fetch menu methods
    // fetchBreakfast()
    //


    class fetchBreakfastMenu extends Thread {
        // thread to do it in the background
        // blank string that will be used to concatenate data
        String data = "";


        @Override
        public void run() {


            menuHandler.post(new Runnable() {
                @Override
                public void run() {
                    // small little thing giving feedback telling the user something is happening
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Fetching Breakfast Menu...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                }
            });

            try {
                // connecting to our backend
                URL url = new URL("https://warrior-dining-server.replit.app/menu");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                // creating an inputstream and bufferedreader to read in data
                InputStream iStream = httpURLConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream));
                String line;

                while ((line = bReader.readLine()) != null) {
                    // while loop to loop through data
                    data = data + line;
                }


                if (!data.isEmpty()) {
                    // JSON stuff to temporarily keep data that is fetched
                    JSONObject jObject = new JSONObject(data);
                    JSONArray menu = jObject.getJSONArray("menus");
                    menuList.clear();

                    for (int i = 0; i < menu.length(); i++) {
                        JSONObject menuItems = menu.getJSONObject(i);
//                        String menuItem = gson.fromJson(String.valueOf(menuItems), (Type) MenuItem.class);
                        String s_menuItem = menuItems.getString("foods");
                        String[] menuArray = s_menuItem.split(",");
                        // menuItem.setMenu(menuArray[i], menuArray[i+1], menuArray[i+2]);

                        String name = Arrays.toString(menuArray[i].split("\\W+", menuArray[i].toCharArray().length-1));
                        String menuItemId = Arrays.toString(menuArray[i+1].split("\\W+", menuArray[i+1].toCharArray().length-1));
                        String category = Arrays.toString(menuArray[i+2].split("\\W+", menuArray[i+2].toCharArray().length-1));

                        String menuInfo = name.replaceAll("[^a-zA-Z]", "") + " " + menuItemId.replaceAll("[^a-zA-Z0-9]", "") + " " + category.replaceAll("[^a-zA-Z]", "");
                        menuList.add(menuInfo);

//                        menuList.add(menuArray[]);

                        for (int j = 0; j < menuArray.length; j++) {
//                            String menuInfo = Arrays.toString(menuArray[j].split("\\W+", menuArray[j].toCharArray().length-1));

                            menuArray[j] = "";
//                            menuList.add(menuInfo);
                        }

                    }
                }

//                if (!data.isEmpty()) {
//                    // JSON stuff to temporarily keep data that is fetched
//                    JSONObject jObject = new JSONObject(data);
//                    JSONArray menu = jObject.getJSONArray("menus");
//                    menuList.clear();
//
//                    for (int i = 0; i < menu.length(); i++) {
//                        JSONObject menuItems = menu.getJSONObject(i);
//                        String menuItem = menuItems.getString("foods");
//                        System.out.println(menuItem);
//                        menuList.add(menuItem);
//                    }
//                }

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
                        // check if progress is showing, aka done
                        progressDialog.dismiss();
                    }
                    menuAdapter.notifyDataSetChanged();

                }
            });

        }
    }

    class fetchMenu extends Thread {
        // thread to do it in the background
        // blank string that will be used to concatenate data
        String data = "";


        @Override
        public void run() {


            menuHandler.post(new Runnable() {
                @Override
                public void run() {
                    // small little thing giving feedback telling the user something is happening
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Fetching Menu...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                }
            });

            try {
                // connecting to our backend
                URL url = new URL("https://warrior-dining-server.replit.app/menu");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                // creating an inputstream and bufferedreader to read in data
                InputStream iStream = httpURLConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream));
                String line;

                while ((line = bReader.readLine()) != null) {
                    // while loop to loop through data
                    data = data + line;
                }

                if (!data.isEmpty()) {
                    // JSON stuff to temporarily keep data that is fetched
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
                        // check if progress is showing, aka done
                        progressDialog.dismiss();
                    }
                    menuAdapter.notifyDataSetChanged();

                }
            });

        }
    }

}