package com.example.busbuddy_droid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.LinkedList;

public class trackList extends AppCompatActivity {
    private busListDBHelper dbHandler;
    private LinkedList<completeStop> favStops;
    private RecyclerView myRecyclerView;
    private busCardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView stationName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_list_layout);
        stationName = findViewById(R.id.Bus_Stop_Location);


    }
}
