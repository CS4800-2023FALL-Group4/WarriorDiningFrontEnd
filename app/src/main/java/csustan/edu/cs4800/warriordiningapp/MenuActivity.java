// *** Author: Daniel Coffland ***

package csustan.edu.cs4800.warriordiningapp;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

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

import csustan.edu.cs4800.warriordiningapp.databinding.ActivityMenuBinding;

// *** Author: Daniel Coffland ***

public class MenuActivity extends AppCompatActivity {

    public String username;

    ActivityMenuBinding binding;    // gotta bind the fragment
    // ArrayList<String> menuList;     // ArrayList for menu later
    List<Map<String, String>> menuList = new ArrayList<Map<String, String>>(); // new and improved menuList
    // using this method to make it easy for inputting subitems into the listview later on
    SimpleAdapter menuAdapter;
    ProgressDialog progressDialog;
    Handler menuHandler = new Handler();
    public MenuItem breakfastMenu[] = new MenuItem[99];
    public MenuItem lunchMenu[];
    public MenuItem dinnerMenu[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());

        // getting the username
        String username = getIntent().getExtras().getString("username");

        Log.d("data passing test", "onCreate: " + username);
        Log.d("data passing test", username.toString());

        // sets the view to main activity
        setContentView(binding.getRoot());

        initializeMenu();
        // onload menu fetch, automatically loads breakfast menu
        // probably will change to add methods for each menu type
        // (i.e. fetchBreakfast(), fetchLunch(), fetchDinner()) X
        new fetchMenu().start();

        binding.fetchBreakfastMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override   // onload method found and created X
            public void onClick(View v) {
                new fetchBreakfastMenu().start();
            }
        });

        binding.fetchLunchMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new fetchLunchMenu().start();
            }
        });

        binding.fetchDinnerMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new fetchDinnerMenu().start();
            }
        });

        RelativeLayout rl = (RelativeLayout)findViewById(R.id.theRelativeLayout);

        binding.darkModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                darkMode();
            }
        });

    }

    private void initializeMenu() {
        // making menu array and adapter to populate the listview
        menuList = new ArrayList<>();
        menuAdapter = new SimpleAdapter(this, menuList, R.layout.testlayout,
                new String[]{"name", "category"},
                new int[]{android.R.id.text1, android.R.id.text2});
        binding.menuList.setAdapter(menuAdapter);
    }

    // in case I need it ever later
    // ?android:attr/listPreferredItemHeight
    // dark mode
    public void darkMode () {
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.theRelativeLayout);
        ListView lv = findViewById(R.id.menuList);
        ColorDrawable getRLColor = (ColorDrawable) rl.getBackground();
        int colorID = getRLColor.getColor();

        if (colorID == (Color.WHITE)) {
            rl.setBackgroundColor(Color.BLACK);
            // best way I've found to change the layout back and forth thus far
            menuList = new ArrayList<>();
            menuAdapter = new SimpleAdapter(this, menuList, R.layout.testlayout2,
                    new String[]{"name", "category"},
                    new int[]{android.R.id.text1, android.R.id.text2});
            binding.menuList.setAdapter(menuAdapter);
            new fetchMenu().start();
        } else if (colorID == (Color.BLACK)) {
            rl.setBackgroundColor(Color.WHITE);
            menuList = new ArrayList<>();
            menuAdapter = new SimpleAdapter(this, menuList, R.layout.testlayout,
                    new String[]{"name", "category"},
                    new int[]{android.R.id.text1, android.R.id.text2});
            binding.menuList.setAdapter(menuAdapter);
            new fetchMenu().start();
        }
    }


    // make separate fetch menu methods
    class fetchBreakfastMenu extends Thread {
        // thread to do it in the background
        // blank string that will be used to concatenate data
        String data = "";

        int menuSize = MenuItem.menuLength(breakfastMenu);

        @Override
        public void run() {

//            if (menuSize == 0) {

            menuHandler.post(new Runnable() {
                @Override
                public void run() {
                    // small little thing giving feedback telling the user something is happening
                    progressDialog = new ProgressDialog(MenuActivity.this);
                    progressDialog.setMessage("Fetching Breakfast Menu...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                }
            });

            try {
                // connecting to our backend
                URL url = new URL("https://warrior-dining-server.replit.app/menu");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("Authorization", (username = getIntent().getExtras().getString("username")));
                httpURLConnection.connect();
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
                    // which menu? 0 = breakfast, 1 = lunch, 2 = dinner
                    JSONObject menuType = menu.getJSONObject(0);
                    // from menuType, make an array for foods
                    JSONArray menuItems = menuType.getJSONArray("foods");
                    JSONArray sortedMenu = new JSONArray();
                    JSONArray unsortedMenu = new JSONArray();

                    // sorting recommendations to the top
                    for (int i = 0; i < menuItems.length(); i++) {
                        JSONObject food = menuItems.getJSONObject(i);
                        String menuItemName = food.getString("name");
                        String menuItemId = food.getString("menuItemId");
                        String menuItemCategory = food.getString("category");


                        if (!menuItemCategory.contains("Recommended")) {
                            sortedMenu.put(food);
                        } else {
                            unsortedMenu.put(food);
                        }
                    }

                    // this works partially... It adds the json arrays together...
                    // but for whatever reason it doesn't sort the way I think it should...
                    // Does listview have its own sorting mechanism?
                    JSONArray fullMenu = new JSONArray();
                    for (int j = 0; j < sortedMenu.length(); j++) {
                        fullMenu.put(sortedMenu.get(j));
                    }

                    for (int k = 0; k < unsortedMenu.length(); k++) {
                        fullMenu.put(unsortedMenu.get(k));
                    }

                    for (int l = 0; l < menuItems.length(); l++) {
                        // from the foods array, get the specific value from the corresponding key
                        JSONObject foodItem = fullMenu.getJSONObject(l);

                        String foodItemName = foodItem.getString("name");
                        String foodItemId = foodItem.getString("menuItemId");
                        String foodItemCategory = foodItem.getString("category");

                        String fullMenuItem = "Item: " + foodItemName + "   Location: " + foodItemCategory;

                        Map<String, String> data = new HashMap<>(2);
                        data.put("category", foodItemCategory);
                        data.put("name", foodItemName);

                        menuList.add(data);
                    }

                }

                // previous attempt to show that I actually do code a lot of different methods
                // but normally end up erasing anything I don't use because it clutters the codebase
//                if (!data.isEmpty()) {
//                    // JSON stuff to temporarily keep data that is fetched
//                    JSONObject jObject = new JSONObject(data);
//                    JSONArray menu = jObject.getJSONArray("menus");
//                    menuList.clear();
//                    JSONObject menuItems = menu.getJSONObject(0);
//
//
//                    for (int i = 0; i < menuItems.length(); i++) {
////                        String menuItem = gson.fromJson(String.valueOf(menuItems), (Type) MenuItem.class);
//                        String s_menuItem = menuItems.getString("foods");
//                        String[] menuArray = s_menuItem.split(",");
//                        // menuItem.setMenu(menuArray[i], menuArray[i+1], menuArray[i+2]);
//
//                        String name = Arrays.toString(menuArray[i].split("\\W+", menuArray[i].toCharArray().length-1));
//                        String menuItemId = Arrays.toString(menuArray[i+1].split("\\W+", menuArray[i+1].toCharArray().length-1));
//                        String category = Arrays.toString(menuArray[i+2].split("\\W+", menuArray[i+2].toCharArray().length-1));
//
//                        String menuInfo = name.replaceAll("[^a-zA-Z0-9\\s]", "") + " " + menuItemId.replaceAll("[^a-zA-Z0-9\\s]", "") + " " + category.replaceAll("[^a-zA-Z0-9\\s]", "") + "\n";
//                        menuList.add(menuInfo);
//
////                        menuList.add(menuArray[]);
//
//                        for (int j = 0; j < menuArray.length; j++) {
//////                            String menuInfo = Arrays.toString(menuArray[j].split("\\W+", menuArray[j].toCharArray().length-1));
////
//                            menuArray[j] = "";
//////                            menuList.add(menuInfo);
//                        }
//
//                    }
//                }

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

// removed excess old code to clean up

    class fetchLunchMenu extends Thread {
        // thread to do it in the background
        // blank string that will be used to concatenate data
        String data = "";


        @Override
        public void run() {


            menuHandler.post(new Runnable() {
                @Override
                public void run() {
                    // small little thing giving feedback telling the user something is happening
                    progressDialog = new ProgressDialog(MenuActivity.this);
                    progressDialog.setMessage("Fetching Lunch Menu...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                }
            });

            try {
                // connecting to our backend
                URL url = new URL("https://warrior-dining-server.replit.app/menu");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("Authorization", (username = getIntent().getExtras().getString("username")));
                httpURLConnection.connect();
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
                    // which menu? 0 = breakfast, 1 = lunch, 2 = dinner
                    JSONObject menuType = menu.getJSONObject(1);
                    // from menuType, make an array for foods
                    JSONArray menuItems = menuType.getJSONArray("foods");
                    JSONArray sortedMenu = new JSONArray();
                    JSONArray unsortedMenu = new JSONArray();

                    // sorting recommendations to the top
                    for (int i = 0; i < menuItems.length(); i++) {
                        JSONObject food = menuItems.getJSONObject(i);
                        String menuItemName = food.getString("name");
                        String menuItemId = food.getString("menuItemId");
                        String menuItemCategory = food.getString("category");


                        if (!menuItemCategory.contains("Recommended")) {
                            sortedMenu.put(food);
                        } else {
                            unsortedMenu.put(food);
                        }
                    }

                    // this works partially... It adds the json arrays together...
                    // but for whatever reason it doesn't sort the way I think it should...
                    // Does listview have its own sorting mechanism?
                    JSONArray fullMenu = new JSONArray();
                    for (int j = 0; j < sortedMenu.length(); j++) {
                        fullMenu.put(sortedMenu.get(j));
                    }

                    for (int k = 0; k < unsortedMenu.length(); k++) {
                        fullMenu.put(unsortedMenu.get(k));
                    }

                    for (int l = 0; l < menuItems.length(); l++) {
                        // from the foods array, get the specific value from the corresponding key
                        JSONObject foodItem = fullMenu.getJSONObject(l);

                        String foodItemName = foodItem.getString("name");
                        String foodItemId = foodItem.getString("menuItemId");
                        String foodItemCategory = foodItem.getString("category");

                        String fullMenuItem = "Item: " + foodItemName + "   Location: " + foodItemCategory;

                        Map<String, String> data = new HashMap<>(2);
                        data.put("category", foodItemCategory);
                        data.put("name", foodItemName);

                        menuList.add(data);
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

    class fetchDinnerMenu extends Thread {
        // thread to do it in the background
        // blank string that will be used to concatenate data
        String data = "";


        @Override
        public void run() {


            menuHandler.post(new Runnable() {
                @Override
                public void run() {
                    // small little thing giving feedback telling the user something is happening
                    progressDialog = new ProgressDialog(MenuActivity.this);
                    progressDialog.setMessage("Fetching Dinner Menu...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                }
            });

            try {
                // connecting to our backend
                URL url = new URL("https://warrior-dining-server.replit.app/menu");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("Authorization", (username = getIntent().getExtras().getString("username")));
                httpURLConnection.connect();
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
                    // which menu? 0 = breakfast, 1 = lunch, 2 = dinner
                    JSONObject menuType = menu.getJSONObject(2);
                    // from menuType, make an array for foods
                    JSONArray menuItems = menuType.getJSONArray("foods");
                    JSONArray sortedMenu = new JSONArray();
                    JSONArray unsortedMenu = new JSONArray();

                    // sorting recommendations to the top
                    for (int i = 0; i < menuItems.length(); i++) {
                        JSONObject food = menuItems.getJSONObject(i);
                        String menuItemName = food.getString("name");
                        String menuItemId = food.getString("menuItemId");
                        String menuItemCategory = food.getString("category");


                        if (!menuItemCategory.contains("Recommended")) {
                            sortedMenu.put(food);
                        } else {
                            unsortedMenu.put(food);
                        }
                    }

                    // this works partially... It adds the json arrays together...
                    // but for whatever reason it doesn't sort the way I think it should...
                    // Does listview have its own sorting mechanism?
                    JSONArray fullMenu = new JSONArray();
                    for (int j = 0; j < sortedMenu.length(); j++) {
                        fullMenu.put(sortedMenu.get(j));
                    }

                    for (int k = 0; k < unsortedMenu.length(); k++) {
                        fullMenu.put(unsortedMenu.get(k));
                    }

                    for (int l = 0; l < menuItems.length(); l++) {
                        // from the foods array, get the specific value from the corresponding key
                        JSONObject foodItem = fullMenu.getJSONObject(l);

                        String foodItemName = foodItem.getString("name");
                        String foodItemId = foodItem.getString("menuItemId");
                        String foodItemCategory = foodItem.getString("category");

                        String fullMenuItem = "Item: " + foodItemName + "   Location: " + foodItemCategory;

                        Map<String, String> data = new HashMap<>(2);
                        data.put("category", foodItemCategory);
                        data.put("name", foodItemName);

                        menuList.add(data);
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
                    progressDialog = new ProgressDialog(MenuActivity.this);
                    progressDialog.setMessage("Fetching Menu...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                }
            });

            try {
                // connecting to our backend
                URL url = new URL("https://warrior-dining-server.replit.app/menu");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("Authorization", (username = getIntent().getExtras().getString("username")));
                httpURLConnection.connect();
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
                    // get current time
                    Date time = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    // format time to just time
                    String currentDateTime = sdf.format(time);
                    String currentTime = currentDateTime.split(" ")[1];
                    String finalTime = "";
                    // format time to just number
                    for (int i = 0 ; i <= 2 ; i++) {
                        finalTime += currentTime.split(":")[i];
                    }

                    // finalTime to int
                    int fTime = Integer.parseInt(finalTime);

                    JSONObject menuType = new JSONObject();

                    String testV = "Things";
                    String testV1 = "Are Messed Up";

                    // which menu? 0-100000 brkfst, 100001-170000 lunch, 170001-235959 dinner
                    // which menu? 0 = breakfast, 1 = lunch, 2 = dinner
                    // JSONObject menuType = menu.getJSONObject(0);
                    if (fTime < 100000) {
                        menuType = menu.getJSONObject(0);
                    } else if (fTime < 170000 && fTime > 100001) {
                        menuType = menu.getJSONObject(1);
                    } else if (fTime < 235959 && fTime > 170001 ) {
                        menuType = menu.getJSONObject(2);
                    } else {
                        menuType = new JSONObject("");
                        Map<String, String> data = new HashMap<>(2);
                        data.put("category", testV);
                        data.put("name", testV1);
                        menuList.add(data);
                    }

                    // from menuType, make an array for foods
                    JSONArray menuItems = menuType.getJSONArray("foods");
                    JSONArray sortedMenu = new JSONArray();
                    JSONArray unsortedMenu = new JSONArray();

                    // sorting recommendations to the top
                    for (int i = 0; i < menuItems.length(); i++) {
                        JSONObject food = menuItems.getJSONObject(i);
                        String menuItemName = food.getString("name");
                        String menuItemId = food.getString("menuItemId");
                        String menuItemCategory = food.getString("category");


                        if (!menuItemCategory.contains("Recommended")) {
                            sortedMenu.put(food);
                        } else {
                            unsortedMenu.put(food);
                        }
                    }

                    // this works partially... It adds the json arrays together...
                    // but for whatever reason it doesn't sort the way I think it should...
                    // Does listview have its own sorting mechanism?
                    JSONArray fullMenu = new JSONArray();
                    for (int j = 0; j < sortedMenu.length(); j++) {
                        fullMenu.put(sortedMenu.get(j));
                    }

                    for (int k = 0; k < unsortedMenu.length(); k++) {
                        fullMenu.put(unsortedMenu.get(k));
                    }

                    for (int l = 0; l < menuItems.length(); l++) {
                        // from the foods array, get the specific value from the corresponding key
                        JSONObject foodItem = fullMenu.getJSONObject(l);

                        String foodItemName = foodItem.getString("name");
                        String foodItemId = foodItem.getString("menuItemId");
                        String foodItemCategory = foodItem.getString("category");

                        String fullMenuItem = "Item: " + foodItemName + "   Location: " + foodItemCategory;

                        // what was I trying to do here...? Make use of the MenuItem object class?
//                        breakfastMenu[i] = new MenuItem(foodItemName, foodItemId, foodItemCategory);
//
//                        Map<String, String> data = new HashMap<>(2);
//                        data.put("category", MenuItem.getCategory(breakfastMenu[i]));
//                        data.put("name", MenuItem.getName(breakfastMenu[i]));

                        Map<String, String> data = new HashMap<>(2);
                        data.put("category", foodItemCategory);
                        data.put("name", foodItemName);

                        menuList.add(data);
                    }

//
//                    for (int i = 0; i < menuItems.length(); i++) {
//                        // from the foods array, get the specific value from the corresponding key
//                        JSONObject food = menuItems.getJSONObject(i);
//                        String menuItemName = food.getString("name");
//                        String menuItemId = food.getString("menuItemId");
//                        String menuItemCategory = food.getString("category");
//
//                        String fullMenuItem = "Item: " + menuItemName + "   Location: " + menuItemCategory;
//
//                        Map<String, String> data = new HashMap<>(2);
//                        data.put("category", menuItemCategory);
//                        data.put("name", menuItemName);
//
//                        menuList.add(data);
//                    }
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