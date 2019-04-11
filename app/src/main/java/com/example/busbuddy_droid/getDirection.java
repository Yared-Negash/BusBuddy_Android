package com.example.busbuddy_droid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class getDirection extends AppCompatActivity {
    TextView displayBus;
    Button directionButton1;
    Button directionButton2;
    String userInputBus1;
    String userInputBus2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.direction_layout);
        displayBus = (TextView) findViewById(R.id.busQuestion);
        directionButton1= (Button) findViewById(R.id.directionOne);
        directionButton2= (Button) findViewById(R.id.directionTwo);



        Intent getBus = getIntent();

        userInputBus1 = getBus.getStringExtra("com.example.busbuddy_droid.busList1");
        userInputBus2 = getBus.getStringExtra("com.example.busbuddy_droid.busList2");

        String stopOne = userInputBus1.substring(userInputBus1.indexOf("|")+1,userInputBus1.length()-1);
        String stopTwo = userInputBus2.substring(userInputBus2.indexOf("|")+1,userInputBus2.length()-1);


        displayBus.setText("You Searched for Bus "+userInputBus1.substring(userInputBus1.indexOf("=")+1
                ,userInputBus1.indexOf("&"))+". In What Direction?\n");
        directionButton1.setText(stopOne);
        directionButton2.setText(stopTwo);

       directionButton1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent passDirection = new Intent(getApplicationContext(),stopList.class);
               //String test = "http://mybusnow.njtransit.com/bustime/wireless/html/"+userInputBus1.substring(0,userInputBus1.indexOf("|"));
               passDirection.putExtra("com.example.busbuddy_droid.direction",userInputBus1);
               startActivity(passDirection);
           }
       });
       directionButton2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent passDirection = new Intent(getApplicationContext(),stopList.class);
               //String test = "http://mybusnow.njtransit.com/bustime/wireless/html/"+userInputBus2.substring(0,userInputBus2.indexOf("|"));
               passDirection.putExtra("com.example.busbuddy_droid.direction",userInputBus2);
               startActivity(passDirection);
           }
       });




    }
}
