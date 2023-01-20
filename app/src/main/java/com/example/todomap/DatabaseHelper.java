package com.example.todomap;

import android.content.Context;
import android.database.SQLException;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "TASKS";

    // Table columns
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String TYPE = "type";
    public static final String TIME = "time";
    public static final String ADDRESS = "address";
    public static final String LAT = "latitude";
    public static final String LON = "longitude";
    public static final String DESC = "description";
//    public static final String STATUS = "status";

    // Database Information
    static final String DB_NAME = "TASKS_TABLE.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE =
            "create table " + TABLE_NAME + "("
                    + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TITLE + " TEXT NOT NULL, "
                    + TYPE + " TEXT, "
                    + TIME + " TEXT, "
                    + ADDRESS + " TEXT, "
                    + LAT + " DOUBLE, "
                    + LON + " DOUBLE, "
                    + DESC + " TEXT "
//                    + STATUS + " INTEGER "
                    + " ); ";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("CREATE_TABLE", "CREATE_TABLE: " + CREATE_TABLE.toString());
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}