package com.example.busbuddy_droid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class viewTracking extends AppCompatActivity {
    private TextView home;
    private TextView temp;
    private trackDB trackDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tracking);
        trackDB = new trackDB(getApplicationContext(),null,null,1);
        temp = findViewById(R.id.tempTextBox);
        home = findViewById(R.id.bus_buddy_logo);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHome = new Intent(getApplicationContext(),MainActivity.class);
                goHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goHome);
            }
        });
        temp.setText(trackDB.databaseToString());
    }
}
