package com.example.busbuddy_droid;


public class busList {

    private String _id;
    private String _street;

    public busList(String _id, String _street) {
        this._id = _id;
        this._street = _street;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_street() {
        return _street;
    }

    public void set_street(String _street) {
        this._street = _street;
    }
}
