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
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

public class viewTracking extends AppCompatActivity {
    private TextView home,temp;
    private trackDB trackDB;


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

    }

    public class updateTrack extends AsyncTask<Void,Void,Void> {
        LinkedList<completeStop> stops = new LinkedList<>();

        @Override
        protected Void doInBackground(Void... voids) {
            stops = trackDB.dbStops();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            myRecyclerView = findViewById(R.id.recylerview_view_tracking_buses);
            myRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mAdapter = new viewTrackingAdapter(stops);
            myRecyclerView.setLayoutManager(mLayoutManager);
            myRecyclerView.setAdapter(mAdapter);
            System.out.println("");
            /*
            mAdapter.setOnItemClickListener(new trackListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    String vehicle = stops.get(0).getBuses().get(position).getVehicle();
                    String stop_id = stops.get(0).getStopID();
                    String bus = stops.get(0).getBuses().get(position).getBus();
                    String direction = stops.get(0).getBuses().get(position).getDirection();
                    String eta = stops.get(0).getBuses().get(position).getETA();

                    Toast toast = Toast.makeText(getApplicationContext(),"You clicked "+vehicle,Toast.LENGTH_SHORT);
                    toast.show();
                    trackingDB.addBus(vehicle,stop_id,bus,direction,eta);

                }
            });
            */

        }
    }
    /*
    public void refreshPage(){
        IntentFilter refreshPage = new IntentFilter();
        refreshPage.addAction(Intent.ACTION_TIME_TICK);
        minRefresh = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getApplicationContext(),"Updating Bus ETA's",Toast.LENGTH_SHORT).show();
                new trackList.updateBus().execute();
            }
        };
        registerReceiver(minRefresh,refreshPage);
    }
    public void manual_Refresh(){

        Toast.makeText(getApplicationContext(),"Manually updating Bus ETA's",Toast.LENGTH_SHORT).show();
        new trackList.updateBus().execute();
        Toast.makeText(getApplicationContext(), "Here is the db \n"+trackingDB.databaseToString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        Toast.makeText(getApplicationContext(),"On Resume Refresh",Toast.LENGTH_SHORT).show();
        super.onResume();
       // manual_Refresh();
       // refreshPage();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(minRefresh);
    }
*/
}
