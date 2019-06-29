package com.example.busbuddy_droid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

//trackList Activity. This is the Activity opene when the user presses the bus icon in the top left corner
// Displays a list of buses that are currently being tracked. Activity will not open of there are no buses being tracked
public class viewTracking extends AppCompatActivity {
    private TextView home, temp;
    private trackDB trackDB;
    private ImageView refresh;
    private BroadcastReceiver minRefresh;
    private RecyclerView myRecyclerView;
    private viewTrackingAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    LinkedList<trackingObject> stops1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tracking);
        trackDB = new trackDB(getApplicationContext(), null, null, 1);
        myRecyclerView = findViewById(R.id.recylerview_view_tracking_buses);
        home = findViewById(R.id.bus_buddy_logo);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHome = new Intent(getApplicationContext(), MainActivity.class);
                goHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goHome);
            }
        });
        temp = findViewById(R.id.testingggg_textview);
        temp.setText(trackDB.databaseToString());
        new updateTrack().execute();
        refresh = findViewById(R.id.refresh_button);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manual_Refresh();
            }
        });
    }

    public class updateTrack extends AsyncTask<Void, Void, Void> {
        LinkedList<trackingObject> stops = trackDB.dbStops();

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < stops.size(); i++) {
                String stopID = stops.get(i).getStopID();
                String vehicle = stops.get(i).getVehicle();

                grabData test = new grabData(stopID);
                String eta = test.trackbusETA(vehicle);
                if (eta == null) {
                    //when the grabdata cant find the eta, we can assume the bus has already arrived, or there was an error. At this poitn we will remove from teh db;
                    //Toast.makeText(getApplicationContext(),"Bus :"+stops.get(i).getBus()+"("+vehicle+") departed. Deleting from trackDB",Toast.LENGTH_SHORT).show();
                    stops.remove(i);
                    trackDB.deleteStop(Integer.parseInt(vehicle));
                    if (stops.size() == 0) {
                        return null;
                    }
                } else {
                    stops.get(i).setETA(eta);
                    trackDB.update_track_ETA(vehicle, eta);
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (stops.size() == 0) {
                Intent goHome = new Intent(getApplicationContext(), MainActivity.class);
                goHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goHome);
            }
            super.onPostExecute(aVoid);
            myRecyclerView = findViewById(R.id.recylerview_view_tracking_buses);
            myRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mAdapter = new viewTrackingAdapter(stops);
            myRecyclerView.setLayoutManager(mLayoutManager);
            myRecyclerView.setAdapter(mAdapter);
            temp.setText(trackDB.databaseToString());
            System.out.println("");

            mAdapter.setOnItemClickListener(new viewTrackingAdapter.OnItemClickListener() {

                @Override
                public void deleteClick(int position) {
                    trackDB.deleteStop(Integer.parseInt(stops.get(position).getVehicle()));
                    Toast.makeText(getApplicationContext(), "Bus :" + stops.get(position).getBus() + "(" + stops.get(position).getVehicle() + ") has been deleted from trackDB", Toast.LENGTH_SHORT).show();
                    stops.remove(position);
                    mAdapter.notifyItemRemoved(position);
                    if (trackDB.numRows() == 0) {
                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
                        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(home);
                    }

                }
            });
            stops1 = this.stops;

        }
    }

    public void refreshPage() {
        IntentFilter refreshPage = new IntentFilter();
        refreshPage.addAction(Intent.ACTION_TIME_TICK);
        minRefresh = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getApplicationContext(), "Updating Bus ETA's", Toast.LENGTH_SHORT).show();
                new updateTrack().execute();
            }
        };
        registerReceiver(minRefresh, refreshPage);
    }

    public void manual_Refresh() {

        Toast.makeText(getApplicationContext(), "Manually updating Bus ETA's", Toast.LENGTH_SHORT).show();
        new updateTrack().execute();
    }

    //turns off trackingService and refreshes
    @Override
    protected void onResume() {
        //Toast.makeText(getApplicationContext(),"On Resume Refresh",Toast.LENGTH_SHORT).show();
        stopTrackService();
        super.onResume();
        manual_Refresh();
        refreshPage();
    }

    //turns on the tracking service
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(minRefresh);
        if (stops1.size() != 0) {
            startTrackService();
        }
    }

    //when the user switches out of this Activity, or out of the app entirely, the service in trackingService is intiaited
    //The user can now get updated about bus ETA's from the notification tray
    public void startTrackService() {
        String database = trackDB.databaseToString();
        Intent serviceIntent = new Intent(getApplicationContext(), trackingService.class);
        serviceIntent.putExtra("dbString", database);
        startService(serviceIntent);
    }

    //stops the trackingService that was in the notification tray
    public void stopTrackService() {
        Intent serviceIntent = new Intent(getApplicationContext(), trackingService.class);
        stopService(serviceIntent);
    }


}
