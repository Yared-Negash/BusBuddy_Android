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
                    String vehicle = "";
                    vehicle = stops.get(0).getBuses().get(position).getVehicle();
                    Toast toast = Toast.makeText(getApplicationContext(),"You clicked "+vehicle,Toast.LENGTH_SHORT);
                    toast.show();

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
