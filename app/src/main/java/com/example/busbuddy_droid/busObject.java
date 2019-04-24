package com.example.busbuddy_droid;

public class busObject {
    private String Bus, Direction;
    private int ETA;

    public busObject(String bus, String direction, int ETA) {
        Bus = bus;
        Direction = direction;
        this.ETA = ETA;
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

    public int getETA() {
        return ETA;
    }

    public void setETA(int ETA) {
        this.ETA = ETA;
    }
}
