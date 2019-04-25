package com.example.busbuddy_droid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;



public class busCardAdapter extends RecyclerView.Adapter<busCardAdapter.ViewHolder> {
    private LinkedList<completeStop> buses;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView stopName, stopID,deleteStop;
        public LinearLayout busLayout;
        busListDBHelper dbHandler;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            //references to views in basic_bus_card.xml
            stopName = itemView.findViewById(R.id.Bus_Stop_Location);
            stopID = itemView.findViewById(R.id.Bus_Stop_ID);
            busLayout = itemView.findViewById(R.id.Bus_ETA_Button_Layout);
            deleteStop = itemView.findViewById(R.id.deleteStop_button);
            deleteStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int stopID = Integer.parseInt(deleteStop.getTag().toString());
                    dbHandler = new busListDBHelper(itemView.getContext(), null, null, 1);
                    dbHandler.deleteStop(stopID);
                    //Intent restart = new Intent(itemView.getContext(),com.example.busbuddy_droid.MainActivity.class);

                    System.exit(0);

                }
            });

        }
    }
    //how to get data from mainacitivty to adatper?: pass to constructor of adapter
    public busCardAdapter(LinkedList<completeStop> passedData) {
        buses = passedData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //passing entire card layout to adapter
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.basic_bus_card,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //pass values to Views in ViewHolder. Retrieved from mainactivity passing info to this
        completeStop currentBus = buses.get(i);
        viewHolder.stopName.setText(currentBus.getStopName());
        viewHolder.stopID.setText(currentBus.getStopID());
        viewHolder.deleteStop.setTag(currentBus.getStopID());
    }

    @Override
    public int getItemCount() {
        //retuns number of bustops
        return buses.size();
    }
}
