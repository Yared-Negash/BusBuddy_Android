package com.example.busbuddy_droid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    EditText busNum;
    EditText direction;
    EditText street;
    public Button submitBus;
    public static String sendText = "com.example.busbuddy_droid.EXTRA_TEXT";
    public static int checkActivity = 1;
    busListDBHelper dbHandler;
    FloatingActionButton fab;
    LinkedList<String> favStops;
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
        else{
            for(int i = 0; i < favStops.size(); i++){

                final TextView busData = new TextView(getApplicationContext());
                busData.setId(Integer.parseInt(favStops.get(i)));
                //busData.setTag(favStops.get(i));
                busData.setText("stop: "+favStops.get(i)+" My TextView ID is :"+busData.getId());
                busData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbHandler.deleteStop(busData.getId());
                        Intent restart = getIntent();
                        finish();
                        startActivity(restart);

                    }
                });
                System.out.print("");
            }
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
                //finish();
                //dbHandler.deleteStop(19553);

            }
        });
        new updateBus().execute();
    }
    /*
    public void updateBus(){

        for(int i = 0; i < favStops.size(); i++){
            //final int finalI = i;
            final int id = Integer.parseInt(favStops.get(i));
            final String stopID = favStops.get(i);
            final String[] response = new String[1];

            Runnable run = new Runnable() {
                @Override
                public void run() {
                    grabData test = new grabData(stopID);
                    LinkedList<busObject> downloadedETA = test.busETA();

                    Message msg = fillETA.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("eta",downloadedETA);
                    bundle.put
                    bundle.putInt("stopID",id);
                    msg.setData(bundle);
                    fillETA.sendMessage(msg);
                }
            };
            Thread thread = new Thread(run);
            thread.start();
        }
    }*/
    public class updateBus extends AsyncTask<Void,Void,Void>{
        LinkedList<completeStop> stops = new LinkedList<>();

        @Override
        protected Void doInBackground(Void... voids) {
            for(int i = 0; i < favStops.size(); i++) {
                //final int finalI = i;
                int id = Integer.parseInt(favStops.get(i));
                final String stopID = favStops.get(i);

                grabData test = new grabData(stopID);
                LinkedList<busObject> downloadedETA = test.busETA();
                completeStop temp = new completeStop(stopID,downloadedETA);
                stops.add(temp);
            }
            /*
            myRecyclerView = findViewById(R.id.recyclerView);
            myRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mAdapter = new busCardAdapter(stops);
            myRecyclerView.setLayoutManager(mLayoutManager);
            myRecyclerView.setAdapter(mAdapter);
            */
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



