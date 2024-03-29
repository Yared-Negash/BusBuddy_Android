package com.example.busbuddy_droid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.LinkedList;

//this class deals with the database containing saved bus stops: reading, adding, deleting, etc
public class busListDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "busList";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_STREET = "street";

    public busListDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    //This is executed to create the table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STREET + " TEXT" +
                ");";

        db.execSQL(query);
    }

    //deletes table. done before creating a new table.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(query);
    }

    //adding row to table
    public void addStop(busList stop) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, stop.get_id());
        values.put(COLUMN_STREET, stop.get_street());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //delete row
    public void deleteStop(int stopID) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + "=\"" + stopID + "\";");
    }

    //returns number of rows (or buses in this case) in the database
    public long numRows() {

        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return count;
    }

    //outputs the contents of the database in a formatted string
    public String databaseToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        ((Cursor) c).moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("id")) != null) {
                dbString += c.getString(c.getColumnIndex("id"));
                dbString += c.getString(c.getColumnIndex("street"));
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }

    //returns true if the given bus is in the database
    public boolean searchStop(String stop) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        ((Cursor) c).moveToFirst();
        while (!c.isAfterLast()) {
            String test = c.getString(c.getColumnIndex("id"));

            if (test.equals(stop)) {
                return true;
            }
            c.moveToNext();
        }
        db.close();
        return false;
    }

    //Returns a LinkedList of every row in the database. Each node in LL represents a busStop and its respective metadata.
    public LinkedList<completeStop> dbStops() {

        LinkedList<completeStop> favoriteStops = new LinkedList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        ((Cursor) c).moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("id")) != null) {
                String stopID = c.getString(c.getColumnIndex("id"));
                String stationName = c.getString(c.getColumnIndex("street"));
                if (stationName.startsWith(" ")) {
                    stationName = stationName.substring(1);
                }
                //stationName = stationName.substring(1);
                favoriteStops.add(new completeStop(stopID, stationName, null));
            }
            c.moveToNext();
        }
        db.close();
        return favoriteStops;
    }
}
