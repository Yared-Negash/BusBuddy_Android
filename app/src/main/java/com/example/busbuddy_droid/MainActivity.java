package com.example.busbuddy_droid;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private busListDBHelper dbHandler;
    private FloatingActionButton fab;
    private LinkedList<completeStop> favStops;
    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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
        new updateBus().execute();
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

        }
    }

}



