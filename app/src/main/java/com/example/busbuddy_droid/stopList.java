package com.example.busbuddy_droid;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

public class stopList extends AppCompatActivity {

    String directionLink;
    LinkedList <String> busLL;
    LinearLayout list;
    busListDBHelper dbHandler;

    public void fillButtons(String response){
        regexFinder obj = new regexFinder();
        String regex = "eta[.][^<]+";
        busLL = obj.findString(regex,response);
        for(int i = 0; i< busLL.size();i++){
            final String street = Jsoup.parse(busLL.get(i).substring( busLL.get(i).indexOf(">")+1)).text();
            final int stopID = Integer.parseInt(busLL.get(i).substring(busLL.get(i).indexOf("id=")+3,busLL.get(i).indexOf("id=")+8));
            final String url = busLL.get(i).substring(0,busLL.get(i).indexOf("\">"));

            Button wow = new Button(getApplicationContext());
            wow.setText(street);
            wow.setTag(url);
            wow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbHandler = new busListDBHelper(getApplicationContext(),null,null,1);
                    if(dbHandler.searchStop(Integer.toString(stopID)) == true){
                        Toast toast = Toast.makeText(getApplicationContext(),"You have already added "+stopID+" at "+street+"." , Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else{
                        busList newStop = new busList(stopID,street);
                        dbHandler.addStop(newStop);
                    }
                    dbHandler.close();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            list.addView(wow);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_list);
        TextView directionQuestion = (TextView)findViewById(R.id.alertDirection);
        list = (LinearLayout) findViewById(R.id.busLayout);

        Intent getDirection = getIntent();
        directionLink = getDirection.getStringExtra("com.example.busbuddy_droid.direction");

        directionQuestion.setText("You're Going to "+directionLink.substring(directionLink.indexOf("|")+1,directionLink.length()-1)+". Which Bus stop?");
        new downloadStops().execute();



    }

    public class downloadStops extends AsyncTask<Void,Void,Void>{
        String value;
        @Override
        protected Void doInBackground(Void... voids) {

            try{
                Document document = Jsoup.connect("http://mybusnow.njtransit.com/bustime/wireless/html/"+directionLink.substring(0,directionLink.indexOf("|"))).get();
                value = document.toString();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            fillButtons(value);
        }
    }
}
