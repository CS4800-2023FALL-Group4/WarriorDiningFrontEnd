package csustan.edu.cs4800.warriordiningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = MainActivity.this;

        File file = new File(context.getFilesDir(), "menu");

        FileReader reader = null;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedReader bReader = new BufferedReader(reader);
        StringBuilder sBuilder = new StringBuilder();
        String line = null;
        try {
            line = bReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (line != null) {
            sBuilder.append(line).append("\n");
            try {
                line = bReader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        try {
            bReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String format = sBuilder.toString();

        JSONObject jObject = null;
        try {
            jObject = new JSONObject(format);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        MenuItem menuItem = null;
        try {
            menuItem = new MenuItem(jObject.get("name").toString(),
                    jObject.get("menuItemId").toString(),
                    jObject.get("category").toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        EditText menuText = findViewById(R.id.menuBreakfast);
        menuText.setText(menuItem.getMenu());

    }


}

//    public void onClick() {
//
