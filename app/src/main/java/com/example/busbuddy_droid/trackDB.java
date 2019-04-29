package com.example.busbuddy_droid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//this class deals the database itself: reading, adding, deleting, etc
public class trackDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "trackList.db";
    private static final String TABLE_NAME = "trackList";
    private static final String COLUMN_VEHICLE = "vehicle";
    private static final String COLUMN_STOPID = "stopid";

    public trackDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override //This is executed to create the table
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " +TABLE_NAME+ "(" +
                COLUMN_VEHICLE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STOPID + " TEXT" +
                ");";

        db.execSQL(query);
    }

    @Override //deletes table. done before creating a new table.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " +TABLE_NAME;
        db.execSQL(query);
    }

    //adding row to table
    public void addBus(String vehicle_id, String stop_id){
        //(Vehicle 5850)
        int vehicle;
        vehicle_id = vehicle_id.substring(9,vehicle_id.length()-1);
        vehicle = Integer.parseInt(vehicle_id);

        ContentValues values = new ContentValues();
        values.put(COLUMN_VEHICLE,vehicle );
        values.put(COLUMN_STOPID,stop_id);
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
        db.close();
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
                dbString += " | "+ c.getString(c.getColumnIndex("stopid"));
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }
    public boolean searchVehicle(String vehicle){
        SQLiteDatabase db = getWritableDatabase();
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
        db.close();
        return false;
    }
    /*
    public LinkedList<completeStop> dbStops(){

        LinkedList<completeStop> favoriteStops = new LinkedList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE 1";

        Cursor c = db.rawQuery(query,null);
        ((Cursor) c).moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("vehicle")) != null){
                String stopID = c.getString(c.getColumnIndex("vehicle"));
                String stationName = c.getString(c.getColumnIndex("stopid"));
                stationName = stationName.substring(1);
                favoriteStops.add(new completeStop(stopID,stationName,null));
            }
            c.moveToNext();
        }
        db.close();
        return favoriteStops;
    }
    */
}
