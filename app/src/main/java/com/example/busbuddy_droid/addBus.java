package com.example.busbuddy_droid;

import android.content.Context;
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

public class addBus extends AppCompatActivity {
    EditText busNum;
    TextView busQuestion;
    Button submitBus;
    TextView testGetReq;
    public static String sendText = "com.example.busbuddy_droid.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_bus);

        busQuestion = (TextView) findViewById(R.id.busQuestion);
        busNum = (EditText) findViewById(R.id.inputBus);
        submitBus = (Button) findViewById(R.id.busButton);
        testGetReq = (TextView) findViewById(R.id.textView2);
        submitBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String busNumber = busNum.getText().toString();

                if(busNumber.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(), "Did You Enter A Bus Yet?", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    new netBus().execute();
                }

            }
        });
    }
    public class netBus extends AsyncTask<String,String,String>{
        String value;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                URL busURL = new URL("http://mybusnow.njtransit.com/bustime/wireless/html/selectdirection.jsp?route="+busNum.getText().toString());

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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            regexFinder obj = new regexFinder();
            LinkedList<String> responseList = obj.findString("selectstop.*>",value);
            testGetReq.setText(value);
        }

    }
}

