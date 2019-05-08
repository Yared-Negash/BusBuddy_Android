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
    //downloads ETA data for each stop
    private String stopID;

    public grabData(String stopID) {
        this.stopID = stopID;
    }
    public LinkedList<busObject> busETA(){
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
            return null;
        }
        else if(data.contains("No arrival times available.")){
            return null;
        }
        else if(data.contains("No service is scheduled for this stop at this time.")){
            return null;
        }
        //span element for vehicle number
        //strong tag for bus Number and eta (check if it has "MIN" inside)
        LinkedList<String> bus = new LinkedList<String>();
        LinkedList<String> directions = regexFinder.findString("To[^&nbsp]+",data);
        LinkedList<String> eta = new LinkedList<String>();
        LinkedList<String> vehicle = new LinkedList<String>();
        LinkedList<busObject> buses = new LinkedList<>();


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
            else{
                eta.add(bold);
            }
        }
        for (Element vehicleNum : span) {
            String bold = vehicleNum.text();
            vehicle.add(bold);
        }
        for(int i = 0; i < directions.size(); i++){
            busObject temp;
            if(eta.get(i).contains("DELAYED")){
                temp = new busObject(bus.get(i),directions.get(i)," has been delayed. Please plan accordingly",vehicle.get(i));
            }
            else if(eta.get(i).contains("<")){
                temp = new busObject(bus.get(i),directions.get(i)," Now",vehicle.get(i));
            }
            else{
                temp = new busObject(bus.get(i),directions.get(i),eta.get(i),vehicle.get(i));
            }
            buses.add(temp);
        }
        System.out.print("");
        return buses;
    }

    public String trackbusETA(String vehicle){
        Document body = null;
        try {
            body = Jsoup.connect("http://mybusnow.njtransit.com/bustime/wireless/html/" +
                    "eta.jsp?route=---&direction=---&displaydirection=---&stop=---&findstop=on&selectedRtpiFeeds=&id="+stopID).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String data = body.toString();

        if(data == ""){
            return null;
        }
        else if(data.contains("No arrival times available.")){
            return null;
        }
        else if(data.contains("No service is scheduled for this stop at this time.")){
            return null;
        }


        //span element for vehicle number
        //strong tag for bus Number and eta (check if it has "MIN" inside)
        LinkedList<String> eta = new LinkedList<String>();
        LinkedList<String> vehicleList = new LinkedList<String>();
        LinkedList<busObject> buses = new LinkedList<>();


        Elements strong = body.getElementsByTag("strong");
        Elements span = body.getElementsByTag("span");

        for (Element arrival : strong) {
            String bold = arrival.text();
            if(bold.contains("#")){
            }
            else if(bold.contains("MIN")){
                eta.add(bold);
            }
            else{
                eta.add(bold);
            }
        }
        for (Element vehicleNum : span) {
            String bold = vehicleNum.text();
            vehicleList.add(bold);
        }
        for(int i = 0; i < vehicleList.size(); i++){
            String temp = vehicleList.get(i);

            temp = temp.substring(9,temp.length()-1);
            if(temp.equals(vehicle)){
                if(eta.get(i).contains("DELAYED")){
                    return "Delayed";
                }
                else if(eta.get(i).contains("< 1 MIN")){
                    return "NOW";
                }
                else{
                    return eta.get(i);
                }
            }
        }

        return null;
    }


}
