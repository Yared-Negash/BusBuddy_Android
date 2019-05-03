package com.example.busbuddy_droid;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import java.net.URI;

public class App extends Application {
    public static final String CHANNEL_ID = "trackingServiceChannel";
    public static final String CHANNEL_ID_NO_SOUND = "trackingServiceChannel_NoSound";


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
            Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.bus_now_sound);
            trackingService.setSound(soundUri,
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .build());
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(trackingService);


            NotificationChannel trackingService_NOSOUND = new NotificationChannel(
                    CHANNEL_ID_NO_SOUND,
                    "Tracking Service Channel NO SOUND",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            trackingService_NOSOUND.setSound(null,null);
            NotificationManager manager_nosound = getSystemService(NotificationManager.class);
            manager_nosound.createNotificationChannel(trackingService_NOSOUND);
        }
    }

}
