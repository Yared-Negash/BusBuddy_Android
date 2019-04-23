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
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class grabData {
    private String stopID;

    public grabData(String stopID) {
        this.stopID = stopID;
    }
    public String busETA(){
        String record = "";
        Document body = null;
        try {
            body = Jsoup.connect("http://mybusnow.njtransit.com/bustime/wireless/html/" +
                    "eta.jsp?route=---&direction=---&displaydirection=---&stop=---&findstop=on&selectedRtpiFeeds=&id="+stopID).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String data = body.toString();

        if(data == ""){
            return "Invalid stop ID. Please try again\n";
        }
        else if(data.contains("No arrival times available.")){
           return "No arrival times available.\n";
        }
        else if(data.contains("No service is scheduled for this stop at this time.")){
            return " No service is scheduled for this stop at this time.\n";
        }
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
        record += "---------------------------------------------------------------------------------------------------\n";
        for(int i = 0; i < directions.size(); i++){
            if(eta.contains("DELAYED")){
                record += bus.get(i)+"("+vehicle.get(i)+") "+directions.get(i)+" has been delayed. Please plan accordingly\n";
            }
            else if(eta.contains("<")){
                record += bus.get(i)+vehicle.get(i)+" "+directions.get(i)+" is arriving in less than 1 Min\n";
            }
            else{
                record += bus.get(i)+vehicle.get(i)+" "+directions.get(i)+" is arriving in "+eta.get(i)+"\n";
            }
            record += "---------------------------------------------------------------------------------------------------\n";
        }
        System.out.print("");
        return record;
    }




}
