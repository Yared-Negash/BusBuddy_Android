package com.example.busbuddy_droid;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_ID = "trackingServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();


        createNotificationChannel();
    }
    private void createNotificationChannel(){
        //creates service
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel trackingService = new NotificationChannel(
                    CHANNEL_ID,
                    "Tracking Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(trackingService);
        }
    }

}
