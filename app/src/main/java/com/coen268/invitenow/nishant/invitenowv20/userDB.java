package com.coen268.invitenow.nishant.invitenowv20;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nishant on 2/21/2015.
 */
public class userDB extends SQLiteOpenHelper {

    /* Schema and Contract */
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_USERNAME = "USERNAME";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_PARSE_OBJECT_ID = "PARSE_OBJECT_ID";
    public static final String COLUMN_FIRSTNAME = "FIRSTNAME";
    public static final String COLUMN_LASTNAME = "LASTNAME";
    public static final String COLUMN_EMAIL = "EMAIL";


    public static final String DATABASE_TABLE = "User";
    public static final int DATABASE_VERSION = 1;
    /* Schema and Contract */

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DATABASE_TABLE;

    private static final String DATABASE_CREATE = "CREATE TABLE "
            + DATABASE_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_USERNAME
            + " TEXT," + COLUMN_PASSWORD + " TEXT," + COLUMN_PARSE_OBJECT_ID + " TEXT,"
            + COLUMN_FIRSTNAME + " TEXT,"
            + COLUMN_LASTNAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT" + ")";

    public userDB(Context context) {
        super(context, DATABASE_TABLE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
