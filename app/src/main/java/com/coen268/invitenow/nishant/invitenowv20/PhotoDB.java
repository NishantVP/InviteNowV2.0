package com.coen268.invitenow.nishant.invitenowv20;

/**
 * Created by Nishant on 3/8/2015.
 */



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class PhotoDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PhotoNotes.db";
    protected static final String TABLE_NAME = "PHOTOS";
    protected static final String UID = "_id";
    protected static final String PIC_FILEPATH = "filepath";
    private static final int DATABASE_VERSION = 4;
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("+ UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PIC_FILEPATH + " VARCHAR(255));";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private Context context;

    public PhotoDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
