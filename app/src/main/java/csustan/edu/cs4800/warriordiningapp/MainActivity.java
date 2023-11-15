package csustan.edu.cs4800.warriordiningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button notificationButton;
    public boolean isCancel = true;
    public boolean activeRequest = false; //use boolean active request for notification
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationButton = findViewById(R.id.notificationButton); //references notificationButton in xml by id

        //special devices compatibility if statement
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Notification1", "Itemnotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //notification code
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "Notification1"); //Notification 1 can be universal
                builder.setContentTitle("Warrior Dining");
                builder.setContentText("Your food is available today!");
                builder.setSmallIcon(R.drawable.ic_launcher_background); //pass new notification icon if you fancy
                builder.setAutoCancel(true); //something for pending intent or whatever
                //boolean toggle
                isCancel = !isCancel;
                if (isCancel = false){
                    activeRequest = true;
                } else {
                    activeRequest = false;
                }
                //following notifies user
                //use following code when api sends out matching menu item
                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);

                private void askNotificationPermission() {
                    // This is only necessary for API level >= 33 (TIRAMISU)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                                PackageManager.PERMISSION_GRANTED) {
                            // FCM SDK (and your app) can post notifications.
                        } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                            // TODO: display an educational UI explaining to the user the features that will be enabled
                            //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                            //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                            //       If the user selects "No thanks," allow the user to continue without notifications.
                        } else {
                            // Directly ask for the permission
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                        }
                    }
                }

                managerCompat.notify(1, builder.build()); //ID can be passed replace (1)

            }
        }
        );}

//    @Override         // commented out for now because of function redundancy
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        android.widget.ImageButton button = (android.widget.ImageButton) findViewById(R.id.prefButton);
//        button.setOnClickListener(this);
//
//    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.breakfastTab:
            // switch fragment to preferences tab and/or preferences screen
                // fragment is like the activity_main ui, a new fragment is like a new page
                // a screen is like an overlay over activity_main, it pauses activity_main
            break;
        }
    }
}