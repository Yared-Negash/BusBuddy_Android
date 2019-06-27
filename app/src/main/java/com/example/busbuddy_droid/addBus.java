package com.example.busbuddy_droid;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

//this class is responsible for sending the getRequest with the given bus

public class addBus extends AppCompatActivity {
    EditText busNum;
    TextView busQuestion;
    Button submitBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_bus);

        //UI elements for the Activity
        busQuestion = (TextView) findViewById(R.id.busQuestion);
        busNum = (EditText) findViewById(R.id.inputBus);
        submitBus = (Button) findViewById(R.id.busButton);
        submitBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String busNumber = busNum.getText().toString();

                if (busNumber.isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Did You Enter A Bus Yet?", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    new netBus().execute();

                    System.out.println();
                }

            }
        });
    }

    public class netBus extends AsyncTask<String, String, String> {
        //Thread off the MAIN UI THREAD tasked with downloading web data
        String value;

        @Override
        protected void onPreExecute() {
            System.out.print("");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            //Makes and attempt to download
            try {
                URL busURL = new URL("http://mybusnow.njtransit.com/bustime/wireless/html/selectdirection.jsp?route=" + busNum.getText().toString());
                HttpURLConnection connect = (HttpURLConnection) busURL.openConnection();
                connect.setRequestMethod("GET");
                connect.connect();

                BufferedReader bf = new BufferedReader((new InputStreamReader(connect.getInputStream())));
                while (bf.readLine() != null) {
                    value += bf.readLine();
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //Method that will parse HTML
            super.onPostExecute(s);
            regexFinder obj = new regexFinder();

            //Given bus will be passed to regEX method findString()
            String regEx = "selectstop[^\"]+";
            LinkedList<String> responseList = obj.findString(regEx, value);
            if (responseList.size() == 0) {
                //The given bus does not exist. Puts a toast on screen that prompts user to input a valid bus
                Toast toast = Toast.makeText(getApplicationContext(), "I could not find that? Try again.", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            Intent passDirections = new Intent(getApplicationContext(), getDirection.class);

            for (int i = 0; i < responseList.size(); i++) {
                //Parses through the data to check if certain URL encoded strings exist. "%2F" for example means "/"
                String passedInfo = responseList.get(i);
                passedInfo = passedInfo.substring(passedInfo.indexOf("n=") + 2, passedInfo.length());
                if (passedInfo.contains("+")) {
                    passedInfo = passedInfo.replace("+", " ");
                }
                if (passedInfo.contains("%2F")) {
                    passedInfo = passedInfo.replace("%2F", "/");
                }
                if (passedInfo.contains("%2C")) {
                    passedInfo = passedInfo.replace("%2C", ",");
                }
                String combine = responseList.get(i) + "|" + passedInfo + "\n";
                passDirections.putExtra("com.example.busbuddy_droid.busList" + (i + 1), combine);
            }
            startActivity(passDirections);

        }


    }
}
