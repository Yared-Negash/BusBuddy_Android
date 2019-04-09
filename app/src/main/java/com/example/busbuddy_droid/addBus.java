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
<<<<<<< HEAD
                //Todo: Try to figure out how to build a proper URL with URI Builder; If string has '&' it will break
                //URL busURL = new URL("https://www.google.com");
                URL busURL = new URL("http://mybusnow.njtransit.com");
=======
                URL busURL = new URL("http://mybusnow.njtransit.com/bustime/wireless/html/selectdirection.jsp?route="+busNum.getText().toString());
>>>>>>> dd65d167888f5f716cd8001059ae01d12246757a

                HttpURLConnection connect = (HttpURLConnection) busURL.openConnection();
                connect.setRequestMethod("GET");
                connect.connect();

                BufferedReader bf = new BufferedReader((new InputStreamReader(connect.getInputStream())));
<<<<<<< HEAD
                        
                value = bf.readLine();
                testGetReq.setText(value);

                //Context context = getApplicationContext();
                System.out.println("DUDE"+value);

            }
            catch (Exception e){
                System.out.println("DIDNT WorK");
                testGetReq.setText("IT didnt work");

            }
            catch (Exception e){
                e.printStackTrace();
>>>>>>> dd65d167888f5f716cd8001059ae01d12246757a
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
<<<<<<< HEAD
=======
            regexFinder obj = new regexFinder();
            value = value.substring(value.indexOf("\t\t\t<a href=\"selectstop"),value.indexOf("-&nbsp;"));
            LinkedList<String> responseList = obj.findString("selectstop.jsp?route=\\d{2,3}&direction=",value);
>>>>>>> dd65d167888f5f716cd8001059ae01d12246757a
            testGetReq.setText(value);
        }


    }
}

