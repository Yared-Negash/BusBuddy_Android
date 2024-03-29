package com.example.busbuddy_droid;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.LinkedList;

//Sets up the adapter for the cards that display tracked buses on
// viewTracking Activity.

public class viewTrackingAdapter extends RecyclerView.Adapter<viewTrackingAdapter.ViewHolder> {
    private LinkedList<trackingObject> buses;

    //mListener contains the Activity
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        //void onItemClick(int position);

        void deleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView stationName, busNum, busDirection, busEta, approxArrival;
        private ImageView deleteButton;
        public LinearLayout busLayout;
        busListDBHelper dbHandler;

        public ViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            //itemView is the actual card

            super(itemView);
            //references to views in track_list_card.xml

            stationName = itemView.findViewById(R.id.bus_station_tracklist_header);
            busNum = itemView.findViewById(R.id.Bus_Number_viewtracked);
            busDirection = itemView.findViewById(R.id.Bus_Direction_viewtracked);
            busEta = itemView.findViewById(R.id.Bus_track_eta_viewtracked);
            approxArrival = itemView.findViewById(R.id.approx_time_viewtracked);
            deleteButton = itemView.findViewById(R.id.delete_tracking_bus);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.deleteClick(position);
                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // listener.onItemClick(position);
                        }
                    }
                }
            });


        }
    }

    //how to get data from mainacitivty to adatper?: pass to constructor of adapter
    public viewTrackingAdapter(LinkedList<trackingObject> passedData) {
        buses = passedData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //passing entire card layout to adapter
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_tracked_bus_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v, mListener);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        //pass values to Views in ViewHolder. Retrieved from mainactivity passing info to this
        trackingObject currentBus = buses.get(i);
        viewHolder.stationName.setText(currentBus.getStopName());

        viewHolder.busNum.setText(currentBus.getBus() + "\n Vehicle: " + currentBus.getVehicle());
        viewHolder.busDirection.setText(currentBus.getDirection());
        viewHolder.busEta.setText(currentBus.getETA());
        String eta = currentBus.getETA();

        try {
            if (eta.contains("Delayed")) {
                viewHolder.busEta.setText("Delayed");
                viewHolder.approxArrival.setText("TBA");
                return;
            }
        } catch (Exception e) {
            System.out.println("tried doing the delay , didnt work");
        }

        if (eta.contains("NOW")) {
            viewHolder.busEta.setText("NOW");
            viewHolder.approxArrival.setText("Now");
            return;
        }

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.add(Calendar.MINUTE, Integer.parseInt(eta.substring(0, eta.indexOf(" "))));
        } catch (Exception e) {
            System.out.println("eta format has changed, now passing raw eta");
            calendar.add(Calendar.MINUTE, Integer.parseInt(eta));
        }
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String approxETA = dateFormat.format(date);

        viewHolder.approxArrival.setText("(" + approxETA + ")");

    }

    @Override
    public int getItemCount() {
        //retuns number of bustops
        return buses.size();
    }
}
