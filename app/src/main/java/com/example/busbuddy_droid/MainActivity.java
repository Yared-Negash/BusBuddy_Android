package com.example.busbuddy_droid;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        busNum = (EditText) findViewById(R.id.busNum);
        direction = (EditText) findViewById(R.id.direction);
        street = (EditText) findViewById(R.id.street);
/*
        startButton = (Button) findViewById(R.id.getRawBus);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Button Has Been Clicked";
                Toast toast = Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT);
                toast.show();
                websiteResponse.setText("You pressed the Button "+buttonCounter+" times! \n ");
            }
        });*/
    }



}



