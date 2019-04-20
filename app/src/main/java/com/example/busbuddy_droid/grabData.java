package com.example.busbuddy_droid;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.widget.TextView;


import java.io.IOException;
import java.util.LinkedList;

public class grabData {
    private String stopID;
    Context context;

    public grabData(String stopID, Context context) {
        this.stopID = stopID;
        this.context = context;
    }
    public TextView busETA(){
        final TextView busData = new TextView(context);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Document body = null;
                try {
                    body = Jsoup.connect("http://mybusnow.njtransit.com/bustime/wireless/html/" +
                            "eta.jsp?route=---&direction=---&displaydirection=---&stop=---&findstop=on&selectedRtpiFeeds=&id="+stopID).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String data = body.toString();
/*
                if(data == ""){
                    console.log("Invalid stopID");
                    whole = " Invalid stop ID. Please try again";
                    callback(whole);
                    return;
                }
                else if(data.contains("No arrival times available.")){
                    console.log("No arrival times available.");
                    busData.setText("No arrival times available.");
                }
                else if(data.includes("No service is scheduled for this stop at this time.")){
                    console.log("No service is scheduled for this stop at this time.");
                    whole = " No service is scheduled for this stop at this time.";
                    callback(whole);
                    return;
                }
*/
                //span element for vehicle number
                //strong tag for bus Number and eta (check if it has "MIN" inside)
                LinkedList<String> bus = new LinkedList<String>();
                LinkedList<String> directions = regexFinder.findString("To[^&nbsp]+",data);
                LinkedList<String> eta = new LinkedList<String>();
                LinkedList<String> vehicle = new LinkedList<String>();


                Elements strong = body.getElementsByTag("strong");
                Elements span = body.getElementsByTag("span");

                for (Element arrival : strong) {
                    String bold = arrival.text();
                    if(bold.contains("#")){
                        bus.add(bold);
                    }
                    else if(bold.contains("MIN")){
                        eta.add(bold);
                    }
                }
                for (Element vehicleNum : span) {
                    String bold = vehicleNum.text();
                    vehicle.add(bold);
                }
                for(int i = 0; i < bus.size(); i++){
                    String record;
                    if(eta.contains("DELAYED")){
                        record = bus.get(i)+"("+vehicle.get(i)+") "+directions.get(i)+" has been delayed. Please plan accordingly";
                    }
                    else if(eta.contains("<")){
                        record = bus.get(i)+vehicle.get(i)+" "+directions.get(i)+" is arriving in less than 1 Min ";
                    }
                    else{
                        record = bus.get(i)+vehicle.get(i)+" "+directions.get(i)+" is arriving in "+eta.get(i);
                    }
                    busData.append(record+" \n");
                    System.out.print("");
                }
                System.out.print("");
            }
        };
        Thread getBody = new Thread(r);
        getBody.start();

        return busData;
    }




}
