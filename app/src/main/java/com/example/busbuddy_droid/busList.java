package com.example.busbuddy_droid;


public class busList {
    /*  object used to pass data to db. Used for adding a new station to favorite stops
        _id variable holds the unique numerical number assigned by NJ transit to each bus stop. For example: 31890
        street is the string that holds the name of the stop. For example:  NEWARK PENN STATION (East Orange/Newark)
    */
    private int _id;
    private String _street;

    public busList(int _id, String _street) {
        this._id = _id;
        this._street = _street;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_street() {
        return _street;
    }

    public void set_street(String _street) {
        this._street = _street;
    }
}
