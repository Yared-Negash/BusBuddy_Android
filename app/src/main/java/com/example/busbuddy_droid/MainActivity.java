package com.example.busbuddy_droid;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    EditText busNum;
    EditText direction;
    EditText street;
    TextView busQuestion;
    public Button submitBus;
    public static String sendText = "com.example.busbuddy_droid.EXTRA_TEXT";
    public static int checkActivity = 1;
    busListDBHelper dbHandler;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new busListDBHelper(getApplicationContext(),null,null,1);
        if(dbHandler.numRows() == 0){
            Intent addBus = new Intent(this, com.example.busbuddy_droid.addBus.class);
            startActivity(addBus);
            finish();
        }
        else{
            //setContentView(R.layout.activity_main);
            busQuestion = (TextView) findViewById(R.id.textView);
            busQuestion.setText(dbHandler.databaseToString());
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
                busQuestion.setText(dbHandler.databaseToString());

            }
        });



       // busQuestion.setText(url);
    }
}



