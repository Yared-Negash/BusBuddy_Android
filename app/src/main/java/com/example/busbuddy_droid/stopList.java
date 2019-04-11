package com.example.busbuddy_droid;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

public class stopList extends AppCompatActivity {

    String directionLink;
    LinearLayout list = (LinearLayout) findViewById(R.id.busLayout);
    LinkedList <String> busLL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_list);
        TextView directionQuestion = (TextView)findViewById(R.id.alertDirection);


        Intent getDirection = getIntent();
        directionLink = getDirection.getStringExtra("com.example.busbuddy_droid.direction");

        directionQuestion.setText("You're Going to "+directionLink.substring(directionLink.indexOf("|")+1,directionLink.length()-1)+". Which Bus stop?");
        //new downloadStops().execute();



    }

    public class downloadStops extends AsyncTask<Void,Void,Void>{
        String value;
        @Override
        protected Void doInBackground(Void... voids) {

            try{
                URL busURL = new URL("http://mybusnow.njtransit.com/bustime/wireless/html/"+directionLink.substring(0,directionLink.indexOf("|")));

                HttpURLConnection connect = (HttpURLConnection) busURL.openConnection();
                connect.setRequestMethod("GET");
                connect.connect();

                BufferedReader bf = new BufferedReader((new InputStreamReader(connect.getInputStream())));
                while(bf.readLine() != null){
                    value += bf.readLine();
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            /*
            super.onPostExecute(aVoid);
            regexFinder obj = new regexFinder();
            String regex = "eta[^<]+";
            busLL = obj.findString(regex,value);
            for(int i = 0; i< busLL.size();i++){
                Button wow = new Button(getApplicationContext());
                wow.setText(busLL.get(i));
                list.addView(wow);
            }*/
        }
    }
}
