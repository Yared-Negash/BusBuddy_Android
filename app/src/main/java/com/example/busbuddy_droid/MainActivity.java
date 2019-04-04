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
    Button startButton;
    TextView busQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        busQuestion = (TextView) findViewById(R.id.busQuestion);
        busNum = (EditText) findViewById(R.id.inputBus);

        setContentView(R.layout.input_bus);


        //direction = (EditText) findViewById(R.id.direction);
        //street = (EditText) findViewById(R.id.street);
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



