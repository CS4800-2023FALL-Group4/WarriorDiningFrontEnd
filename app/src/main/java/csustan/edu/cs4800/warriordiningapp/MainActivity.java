package csustan.edu.cs4800.warriordiningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    GetMenu getMenu;

    @Override
    public void onAttachFragment(Context context) {
        super.onAttachFragment(context.this);
        getMenu = (GetMenu) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // bundle used as a method of creating a container to pass data from fragmentA to fragmentB
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.widget.ImageButton button = (android.widget.ImageButton) findViewById(R.id.prefButton);
        button.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.prefButton;
            // switch fragment to preferences tab and/or preferences screen
                // fragment is like the activity_main ui, a new fragment is like a new page
                // a screen is like an overlay over activity_main, it pauses activity_main
            break;
        }
    }
}