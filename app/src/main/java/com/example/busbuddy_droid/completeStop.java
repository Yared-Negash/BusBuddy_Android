package com.example.busbuddy_droid;

import java.util.LinkedList;

public class completeStop {
    private String stopID;
    private LinkedList<busObject> buses;

    public String getStopID() {
        return stopID;
    }

    public void setStopID(String stopID) {
        this.stopID = stopID;
    }

    public LinkedList<busObject> getBuses() {
        return buses;
    }

    public void setBuses(LinkedList<busObject> buses) {
        this.buses = buses;
    }

    public completeStop(String stopID, LinkedList<busObject> buses) {
        this.stopID = stopID;
        this.buses = buses;
    }
}
