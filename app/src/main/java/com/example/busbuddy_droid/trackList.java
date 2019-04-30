package com.example.busbuddy_droid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

public class trackList extends AppCompatActivity {
    private busListDBHelper dbHandler;
    private trackListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView myRecyclerView;
    private ImageView refresh_button;
    private TextView stationName;
    private String stopID;
    private String stopName;
    private BroadcastReceiver minRefresh;
    private trackDB trackingDB;

    private ImageView view_tracked_buses;
    private TextView home;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_list_layout);
        stationName = findViewById(R.id.Bus_Stop_Location);
        refresh_button = findViewById(R.id.refresh_button);
        Intent temp = getIntent();
        stopID = temp.getStringExtra("stopID");
        stopName = temp.getStringExtra("stopName");
        stationName.setText(stopName);
        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manual_Refresh();
            }
        });
        trackingDB = new trackDB(getApplicationContext(),null,null,1);
        view_tracked_buses = findViewById(R.id.check_tracked_button);
        view_tracked_buses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trackingDB.numRows() == 0){
                    Toast.makeText(getApplicationContext(),"You have not tracked a Bus yet.",Toast.LENGTH_LONG).show();
                }
                else{
                    Intent changetoTrackActivity = new Intent(getApplicationContext(),viewTracking.class);
                    startActivity(changetoTrackActivity);
                }
            }
        });
        home = findViewById(R.id.bus_buddy_logo);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHome = new Intent(getApplicationContext(),MainActivity.class);
                goHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goHome);
            }
        });

    }

    public class updateBus extends AsyncTask<Void,Void,Void> {
        LinkedList<completeStop> stops = new LinkedList<>();

        @Override
        protected Void doInBackground(Void... voids) {
            completeStop temp = new completeStop(stopID,stopName,new grabData(stopID).busETA());
            stops.add(temp);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            myRecyclerView = findViewById(R.id.tracklist_recyclerView);
            myRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mAdapter = new trackListAdapter(stops);
            myRecyclerView.setLayoutManager(mLayoutManager);
            myRecyclerView.setAdapter(mAdapter);
            System.out.println("");

            mAdapter.setOnItemClickListener(new trackListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    String vehicle = stops.get(0).getBuses().get(position).getVehicle();
                    String stop_id = stops.get(0).getStopID();
                    String bus = stops.get(0).getBuses().get(position).getBus();
                    String direction = stops.get(0).getBuses().get(position).getDirection();
                    String eta = stops.get(0).getBuses().get(position).getETA();
                    String stationName = stops.get(0).getStopName();

                    if(trackingDB.searchVehicle(vehicle) == true){
                        trackingDB.updateETA(vehicle,eta);
                        Toast.makeText(getApplicationContext(),"You updated "+vehicle,Toast.LENGTH_SHORT).show();
                    }
                    else{
                        trackingDB.addBus(vehicle,stop_id,bus,direction,eta,stationName);
                        Toast.makeText(getApplicationContext(),"You added to the DB:  "+vehicle,Toast.LENGTH_SHORT).show();

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
                new updateBus().execute();
            }
        };
        registerReceiver(minRefresh,refreshPage);
    }
    public void manual_Refresh(){

        Toast.makeText(getApplicationContext(),"Manually updating Bus ETA's",Toast.LENGTH_SHORT).show();
        new updateBus().execute();
        Toast.makeText(getApplicationContext(), "Here is the db \n"+trackingDB.databaseToString(), Toast.LENGTH_SHORT).show();
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
