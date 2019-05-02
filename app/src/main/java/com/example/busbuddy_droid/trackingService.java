package com.example.busbuddy_droid;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import java.util.LinkedList;

import static com.example.busbuddy_droid.App.CHANNEL_ID;


public class trackingService extends Service {
    private trackDB trackDB;
    private LinkedList<trackingObject> stops;
    private BroadcastReceiver minRefresh;


    //@androidx.annotation.Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //called every time we call a startService. Can be called multiple times

        if(stops.size() == 0){
            stopSelf();
        }
        String eta_text = "";
        Intent viewTrackList = new Intent(getApplicationContext(),viewTracking.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0,viewTrackList,0);
        for(int i = 0; i < stops.size(); i++){
            String bus = stops.get(i).getBus();
            String stopName = stops.get(i).getStopName();
            String eta = stops.get(i).getETA();

            eta_text += bus + " at "+ stopName + " is arriving in "+ eta+ '\n';
        }

        Notification notification = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle("Tracked Buses")
                .setStyle(
                        new NotificationCompat.BigTextStyle()
                        .bigText(eta_text)
                )
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
        trackDB = new trackDB(getApplicationContext(),null,null,1);
        stops = trackDB.dbStops();
        IntentFilter refreshPage = new IntentFilter();
        refreshPage.addAction(Intent.ACTION_TIME_TICK);
        minRefresh = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                new updateTrack().execute();
            }
        };
        registerReceiver(minRefresh,refreshPage);
    }
    public class updateTrack extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            for(int i = 0; i < stops.size(); i++) {
                String eta;
                String stopID = stops.get(i).getStopID();
                String vehicle = stops.get(i).getVehicle();
                grabData test = new grabData(stopID);

                eta = test.trackbusETA(vehicle);
                if(eta == null){
                    //when the grabdata cant find the eta, we can assume the bus has already arrived, or there was an error. At this poitn we will remove from teh db;
                    stops.remove(i);
                    trackDB.deleteStop(Integer.parseInt(vehicle));
                    if(stops.size() == 0){
                        return null;
                    }
                }
                else{
                    stops.get(i).setETA(eta);
                    trackDB.update_track_ETA(vehicle,eta);
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent serviceIntent = new Intent(getApplicationContext(),trackingService.class);
            startService(serviceIntent);

        }
    }
}
