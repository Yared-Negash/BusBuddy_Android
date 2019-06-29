package com.example.busbuddy_droid;

//object that holds stopID, stopName, Bus, and an other metadata. used in the viewTracking class
public class trackingObject {
    private String stopID, stopName;
    private String Bus, Direction, ETA, Vehicle;


    public trackingObject(String stopID, String stopName, String bus, String direction, String ETA, String vehicle) {
        this.stopID = stopID;
        this.stopName = stopName;
        Bus = bus;
        Direction = direction;
        this.ETA = ETA;
        Vehicle = vehicle;
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

    public String getBus() {
        return Bus;
    }

    public void setBus(String bus) {
        Bus = bus;
    }

    public String getDirection() {
        return Direction;
    }

    public void setDirection(String direction) {
        Direction = direction;
    }

    public String getETA() {
        return ETA;
    }

    public void setETA(String ETA) {
        this.ETA = ETA;
    }

    public String getVehicle() {
        return Vehicle;
    }

    public void setVehicle(String vehicle) {
        Vehicle = vehicle;
    }
}
