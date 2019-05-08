package com.example.busbuddy_droid;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;



public class busCardAdapter extends RecyclerView.Adapter<busCardAdapter.ViewHolder> {
    private LinkedList<completeStop> buses;

    //mListener contains the Activity
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void deleteClick(int position);


    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView stopName, stopID,busETA;
        public ImageView deleteStop;
        public LinearLayout busLayout;
        busListDBHelper dbHandler;

        public ViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            //itemView is the actual card

            super(itemView);
            //references to views in basic_bus_card.xml
            stopName = itemView.findViewById(R.id.Bus_Stop_Location);
            stopID = itemView.findViewById(R.id.Bus_Stop_ID);
            busLayout = itemView.findViewById(R.id.Bus_ETA_Layout);
            busETA = itemView.findViewById(R.id.etaTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            deleteStop = itemView.findViewById(R.id.deleteStop_button);
            deleteStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.deleteClick(position);
                        }
                    }
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
        ViewHolder viewHolder = new ViewHolder(v,mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        //pass values to Views in ViewHolder. Retrieved from mainactivity passing info to this
        completeStop currentBus = buses.get(i);
        String output = "";
        if (currentBus.getBuses() == null) {
            output = "No service is scheduled for this stop at this time.\n";
        }
        else{
            for(int j = 0; j < buses.get(i).getBuses().size(); j++){
                busObject temp = buses.get(i).getBuses().get(j);
                output += (j+1)+") Bus "+ temp.getBus()+" "+temp.getDirection()+" is arriving in "+temp.getETA()+"\n\n";
            }
        }
        viewHolder.stopName.setText(currentBus.getStopName());
        viewHolder.stopID.setText(currentBus.getStopID());
        viewHolder.busETA.setText(output);
        viewHolder.deleteStop.setTag(currentBus.getStopID());

    }

    @Override
    public int getItemCount() {
        //retuns number of bustops
        return buses.size();
    }
}
