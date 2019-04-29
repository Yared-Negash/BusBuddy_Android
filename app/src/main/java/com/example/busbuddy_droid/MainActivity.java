package com.example.busbuddy_droid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private busListDBHelper dbHandler;
    private FloatingActionButton fab;
    private LinkedList<completeStop> favStops;
    private RecyclerView myRecyclerView;
    private busCardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private BroadcastReceiver minRefresh;
    private ImageView refresh;
    private ImageView view_tracked_buses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dbHandler = new busListDBHelper(getApplicationContext(),null,null,1);
        favStops = dbHandler.dbStops();

        if(dbHandler.numRows() == 0){
            Intent addBus = new Intent(this, com.example.busbuddy_droid.addBus.class);
            startActivity(addBus);
            finish();
        }
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbHandler.numRows() >= 5){
                    Snackbar.make(view, "You've reached the maximum number of Bus Stops. Please Delete a stop to continue", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                Intent addAnotherbus = new Intent(getApplicationContext(), com.example.busbuddy_droid.addBus.class);
                startActivity(addAnotherbus);
            }
        });
        //new updateBus().execute();
        refresh = findViewById(R.id.refresh_button);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manual_Refresh();
            }
        });
        view_tracked_buses = findViewById(R.id.check_tracked_button);
        view_tracked_buses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackDB test = new trackDB(getApplicationContext(),null,null,1);
                if(test.numRows() == 0){
                    Toast.makeText(getApplicationContext(),"You have not tracked a Bus yet.",Toast.LENGTH_LONG).show();
                }
                else{
                    Intent changetoTrackActivity = new Intent(getApplicationContext(),viewTracking.class);
                    startActivity(changetoTrackActivity);
                }
            }
        });
    }

    public class updateBus extends AsyncTask<Void,Void,Void>{
        LinkedList<completeStop> stops = new LinkedList<>();

        @Override
        protected Void doInBackground(Void... voids) {
            for(int i = 0; i < favStops.size(); i++) {
                final String stationName = favStops.get(i).getStopName();
                final String stopID = favStops.get(i).getStopID();

                grabData test = new grabData(stopID);
                LinkedList<busObject> downloadedETA = test.busETA();

                completeStop temp = new completeStop(stopID,stationName,downloadedETA);
                stops.add(temp);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            myRecyclerView = findViewById(R.id.recyclerView);
            myRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mAdapter = new busCardAdapter(stops);
            myRecyclerView.setLayoutManager(mLayoutManager);
            myRecyclerView.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(new busCardAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    stops.get(position);
                    Toast toast = Toast.makeText(getApplicationContext(),"You clicked card "+stops.get(position).getStopName(),Toast.LENGTH_SHORT);
                    toast.show();
                    if(stops.get(position).getBuses() == null){
                        Toast.makeText(getApplicationContext(),"No buses available for "+stops.get(position).getStopName(),Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent trackBus = new Intent(getApplicationContext(), com.example.busbuddy_droid.trackList.class);
                    trackBus.putExtra("stopID",favStops.get(position).getStopID());
                    trackBus.putExtra("stopName",favStops.get(position).getStopName());
                    startActivity(trackBus);

                }

                @Override
                public void deleteClick(int position) {
                    dbHandler.deleteStop(Integer.parseInt(stops.get(position).getStopID()));

                    stops.remove(position);
                    mAdapter.notifyItemRemoved(position);
                    favStops = dbHandler.dbStops();

                    Toast toast = Toast.makeText(getApplicationContext(),"You are deleted card "+position,Toast.LENGTH_SHORT);
                    toast.show();
                    if(dbHandler.numRows() == 0){
                        Intent addBus = new Intent(getApplicationContext(), com.example.busbuddy_droid.addBus.class);
                        startActivity(addBus);
                        finish();
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



