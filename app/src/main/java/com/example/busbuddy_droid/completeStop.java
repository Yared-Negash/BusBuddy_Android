package com.example.busbuddy_droid;

import java.util.LinkedList;

public class completeStop {
    //object passed to adapter to display street name, stop id, and busTimes
    private String stopID, stopName;
    private LinkedList<busObject> buses;

    public completeStop(String stopID, String stopName, LinkedList<busObject> buses) {
        this.stopID = stopID;
        this.stopName = stopName;
        this.buses = buses;
    }

    public String getStopID() {
        return stopID;
    }

    public void setStopID(String stopID) {
        this.stopID = stopID;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public LinkedList<busObject> getBuses() {
        return buses;
    }

    public void setBuses(LinkedList<busObject> buses) {
        this.buses = buses;
    }
}
