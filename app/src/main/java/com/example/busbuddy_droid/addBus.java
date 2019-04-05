package com.example.busbuddy_droid;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
                //String message = busNum.getText().toString();
                //Intent passBus = new Intent(addBus.this,getDirection.class);
               // passBus.putExtra(sendText,message);
               // startActivity(passBus);
                new netBus().execute();


            }
        });
    }
    public class netBus extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                //Todo: Try to figure out how to build a proper URL with URI Builder; If string has '&' it will break
                URL busURL = new URL("http://www.mybusnow.njtransit.com");

                HttpURLConnection connect = (HttpURLConnection) busURL.openConnection();
                connect.setRequestMethod("GET");
                connect.connect();

                BufferedReader bf = new BufferedReader((new InputStreamReader(connect.getInputStream())));
                String value = bf.readLine();
                testGetReq.setText(value);

                Context context = getApplicationContext();
                System.out.println("DUDE"+value);

            }
            catch (Exception e){
                System.out.println("DIDNT WorK");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }
}

