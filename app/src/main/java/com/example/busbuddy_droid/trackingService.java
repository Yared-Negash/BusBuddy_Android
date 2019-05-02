package com.example.busbuddy_droid;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import static com.example.busbuddy_droid.App.CHANNEL_ID;

public class trackingService extends Service {
    //@androidx.annotation.Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //called every time we call a startService. Can be called multiple times

        String database_text = intent.getStringExtra("dbString");
        Intent viewTrackList = new Intent(getApplicationContext(),viewTracking.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0,viewTrackList,0);

        Notification notification = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle("trackingServiceNotification")
                .setContentText(database_text)
                .setSmallIcon(R.drawable.ic_directions_bus_tracking_service_icon)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        //Only called when service is stopped. When this service is called again it would
        //call onCreate
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        //will be called when service is created for the first time. Called once
        super.onCreate();
    }
}
