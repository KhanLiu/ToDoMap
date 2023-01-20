package com.example.todomap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public String _ID = "_id";
    public String TITLE = "title";
    public String TYPE = "type";
    public String TIME = "time";
    public String ADDRESS = "address";
    public String LAT = "latitude";
    public String LON = "longitude";
    public String DESC = "description";

    // Insert a new row into table
    public void insert(String title, String type, String time, String address, Double lat, Double lon, String desc) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.TITLE, title);
        contentValue.put(DatabaseHelper.TYPE, type);
        contentValue.put(DatabaseHelper.TIME, time);
        contentValue.put(DatabaseHelper.ADDRESS, address);
        contentValue.put(DatabaseHelper.LAT, lat);
        contentValue.put(DatabaseHelper.LON, lon);
        contentValue.put(DatabaseHelper.DESC, desc);

        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    // Fetch all rows from table
    public Cursor fetch() {
        String[] columns = new String[] {
                DatabaseHelper._ID,
                DatabaseHelper.TITLE,
                DatabaseHelper.TYPE,
                DatabaseHelper.TIME,
                DatabaseHelper.ADDRESS,
                DatabaseHelper.LAT,
                DatabaseHelper.LON,
                DatabaseHelper.DESC,
        };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    // Update a row into table
    public int update(int _id, String title, String type, String time, String address, Double lat, Double lon, String desc) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.TITLE, title);
        contentValue.put(DatabaseHelper.TYPE, type);
        contentValue.put(DatabaseHelper.TIME, time);
        contentValue.put(DatabaseHelper.ADDRESS, address);
        contentValue.put(DatabaseHelper.LAT, lat);
        contentValue.put(DatabaseHelper.LON, lon);
        contentValue.put(DatabaseHelper.DESC, desc);

        int i = database.update(DatabaseHelper.TABLE_NAME, contentValue, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }


    // Delete a row from table
    public void delete(int _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

}
