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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.direction_layout);
        displayBus = (TextView) findViewById(R.id.busQuestion);
        Button goBack = (Button)findViewById(R.id.goToBusActivty);


        Intent getBus = getIntent();
        //Todo learn how to retreive data from an intent. addbus.some variable?
        String userInputBus = getBus.getStringExtra(addBus.sendText);
        displayBus.setText("You Searched for Bus #"+userInputBus+". In What Direction?");

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent test = new Intent(getDirection.this,addBus.class);
                startActivity(test);
            }
        });



    }
}
