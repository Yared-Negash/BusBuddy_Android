package com.example.busbuddy_droid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

//this class deals the database itself: reading, adding, deleting, etc
public class trackDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "trackList.db";
    private static final String TABLE_NAME = "trackList";

    private static final String COLUMN_VEHICLE = "vehicle";
    private static final String COLUMN_STOPID = "stopid";
    private static final String COLUMN_BUSNUM = "bus";
    private static final String COLUMN_Direction = "direction";
    private static final String COLUMN_ETA = "eta";
    private static final String COLUMN_STATIONNAME = "stationname";



    public trackDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override //This is executed to create the table
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " +TABLE_NAME+ "(" +
                COLUMN_VEHICLE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STATIONNAME + " TEXT, "+
                COLUMN_STOPID + " TEXT, " +
                COLUMN_BUSNUM + " TEXT, " +
                COLUMN_Direction + " TEXT, "+
                COLUMN_ETA + " TEXT"+
                ");";

        db.execSQL(query);
    }

    @Override //deletes table. done before creating a new table.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " +TABLE_NAME;
        db.execSQL(query);
    }

    //adding row to table
    public void addBus(String vehicle_id, String stop_id, String bus, String direction, String eta,String stationName){
        //(Vehicle 5850)
        if(searchVehicle(vehicle_id) == true){
            return;
        }

        int vehicle;
        vehicle_id = vehicle_id.substring(9,vehicle_id.length()-1);
        vehicle = Integer.parseInt(vehicle_id);

        ContentValues values = new ContentValues();
        values.put(COLUMN_VEHICLE,vehicle );
        values.put(COLUMN_STOPID,stop_id);
        values.put(COLUMN_BUSNUM,bus);
        values.put(COLUMN_Direction,direction);
        values.put(COLUMN_ETA,checkETA(eta));
        values.put(COLUMN_STATIONNAME, stationName);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME,null,values);
        db.close();
    }
    //delete row
    public void deleteStop(int vehicle_id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME+ " WHERE "+COLUMN_VEHICLE+ "=\""+vehicle_id+ "\";");
    }
    public long numRows(){

        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        //db.close();
        return count;
    }

    public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE 1";

        Cursor c = db.rawQuery(query,null);
        ((Cursor) c).moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("vehicle")) != null){
                dbString += c.getString(c.getColumnIndex("vehicle"));
                dbString += " | "+ c.getString(c.getColumnIndex("stationname"));
                dbString += " | "+ c.getString(c.getColumnIndex("stopid"));
                dbString += " | "+ c.getString(c.getColumnIndex("bus"));
                dbString += " | "+ c.getString(c.getColumnIndex("direction"));
                dbString += " | "+ c.getString(c.getColumnIndex("eta"));
                dbString += "\n";
            }
            c.moveToNext();
        }
        //db.close();
        return dbString;
    }
    public boolean searchVehicle(String vehicle){
        SQLiteDatabase db = getWritableDatabase();
        vehicle = vehicle.substring(9,vehicle.length()-1);
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE 1";

        Cursor c = db.rawQuery(query,null);
        ((Cursor) c).moveToFirst();
        while(!c.isAfterLast()){
            String test = c.getString(c.getColumnIndex("vehicle"));
            if(test.equals(vehicle)){
                return true;
            }
            c.moveToNext();
        }
        //db.close();
        return false;
    }

    public void updateETA(String vehicle, String eta){
        if(searchVehicle(vehicle) == true){
            vehicle = vehicle.substring(9,vehicle.length()-1);
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_ETA,checkETA(eta));
            db.update(TABLE_NAME,values,COLUMN_VEHICLE+"="+vehicle,null);
           //db.close();
        }
    }
    public void update_track_ETA(String vehicle, String eta){
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            if(eta.equals("NOW")){
                values.put(COLUMN_ETA,eta);
            }
            else{
                values.put(COLUMN_ETA,checkETA(eta));
            }
            db.update(TABLE_NAME,values,COLUMN_VEHICLE+"="+vehicle,null);
            //db.close();

    }
    public String checkETA(String eta){
        try{
            if(eta.contains(" has been delayed. Please plan accordingly\n")){
                return "Delayed";
            }

            if(eta.contains("Delayed")){
                return "Delayed";
            }
        }catch (Exception e){
            System.out.println("tried doing the delay , didnt work");
            Log.e(TAG, "checkETA: has failed. it is: "+eta, e);
        }
        eta = eta.substring(0,eta.indexOf(" "));
        if(eta.equals("")){
            eta = "1";
        }
        return eta;
    }

    public LinkedList<trackingObject> dbStops(){

        LinkedList<trackingObject> viewTracking = new LinkedList<>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE 1";

        Cursor c = db.rawQuery(query,null);
        ((Cursor) c).moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("vehicle")) != null){


                String vehicle = c.getString(c.getColumnIndex("vehicle"));
                String stopID = c.getString(c.getColumnIndex("stopid"));
                String bus = c.getString(c.getColumnIndex("bus"));
                String direction = c.getString(c.getColumnIndex("direction"));
                String ETA = c.getString(c.getColumnIndex("eta"));
                String stopName = c.getString(c.getColumnIndex("stationname"));

                //String stopID, String stopName, String bus, String direction, String ETA, String vehicle

                viewTracking.add(new trackingObject(stopID,stopName,bus,direction,ETA,vehicle));
            }
            c.moveToNext();
        }
        //db.close();
        return viewTracking;
    }
}
