package com.example.busbuddy_droid;

public class busObject {
    //object that hold data for each ETA record such as bus, direction it is heading in, the vehicle number, and its Estimated Time of Arrival
    private String Bus, Direction,ETA, Vehicle;

    public busObject(String bus, String direction, String ETA, String vehicle) {
        Bus = bus;
        Direction = direction;
        this.ETA = ETA;
        Vehicle = vehicle;
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
