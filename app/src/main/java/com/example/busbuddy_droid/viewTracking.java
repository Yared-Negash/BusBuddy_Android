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

public class viewTracking extends AppCompatActivity {
    private TextView home,temp;
    private trackDB trackDB;
    private ImageView refresh;
    private BroadcastReceiver minRefresh;
    private RecyclerView myRecyclerView;
    private viewTrackingAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tracking);
        trackDB = new trackDB(getApplicationContext(),null,null,1);
        myRecyclerView = findViewById(R.id.recylerview_view_tracking_buses);
        home = findViewById(R.id.bus_buddy_logo);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHome = new Intent(getApplicationContext(),MainActivity.class);
                goHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    public class updateTrack extends AsyncTask<Void,Void,Void> {
        LinkedList<trackingObject> stops = new LinkedList<>();

        @Override
        protected Void doInBackground(Void... voids) {
            stops = trackDB.dbStops();
            for(int i = 0; i < stops.size(); i++) {
                String stopID = stops.get(i).getStopID();
                String vehicle = stops.get(i).getVehicle();

                grabData test = new grabData(stopID);
                String eta = test.trackbusETA(vehicle);
                if(eta == null){
                    //when the grabdata cant find the eta, we can assume the bus has already arrived, or there was an error. At this poitn we will remove from teh db;
                    stops.remove(i);
                    trackDB.deleteStop(Integer.parseInt(vehicle));
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

            if(stops.size() == 0){
                Intent goHome = new Intent(getApplicationContext(),MainActivity.class);
                goHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                    stops.remove(position);
                    mAdapter.notifyItemRemoved(position);
                    if(trackDB.numRows() == 0){
                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
                        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(home);
                    }

                }
            });

        }
    }

    public void refreshPage(){
        IntentFilter refreshPage = new IntentFilter();
        refreshPage.addAction(Intent.ACTION_TIME_TICK);
        minRefresh = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getApplicationContext(),"Updating Bus ETA's",Toast.LENGTH_SHORT).show();
                new updateTrack().execute();
            }
        };
        registerReceiver(minRefresh,refreshPage);
    }
    public void manual_Refresh(){

        Toast.makeText(getApplicationContext(),"Manually updating Bus ETA's",Toast.LENGTH_SHORT).show();
        new updateTrack().execute();
    }

    @Override
    protected void onResume() {
        Toast.makeText(getApplicationContext(),"On Resume Refresh",Toast.LENGTH_SHORT).show();
        super.onResume();
        manual_Refresh();
        refreshPage();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(minRefresh);
    }

}
