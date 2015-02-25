package com.coen268.invitenow.nishant.invitenowv20;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nishant on 2/24/2015.
 */
public class friendLocationDB extends SQLiteOpenHelper {

    /* Schema and Contract */
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_FRIEND_USERNAME = "FRIEND_USERNAME";
    public static final String COLUMN_FRIEND_LAT = "FRIEND_LAT";
    public static final String COLUMN_FRIEND_LNG = "FRIEND_LNG";
    public static final String COLUMN_FRIEND_FIRSTNAME = "FRIEND_FIRSTNAME";
    public static final String COLUMN_FRIEND_LASTNAME = "FRIEND_LASTNAME";
    public static final String COLUMN_FRIEND_EMAIL = "FRIEND_EMAIL";


    public static final String DATABASE_TABLE = "Friends";
    public static final int DATABASE_VERSION = 1;
    /* Schema and Contract */

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DATABASE_TABLE;


    /*
    private static final String DATABASE_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "  %s integer primary key autoincrement, " + " %s text,"+
                    "  %s text," + " %s text," + " %s text,"+ " %s text,"+
                    "  %s text)",
            DATABASE_TABLE, COLUMN_ID, COLUMN_USERNAME, COLUMN_PASSWORD,COLUMN_PARSE_OBJECT_ID,
            COLUMN_FIRSTNAME,COLUMN_LASTNAME,COLUMN_EMAIL );
    */

    /* private static final String DATABASE_CREATE = "create table "
             + "(" + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_COMMENT
             + " text not null);";
 */
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + DATABASE_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_FRIEND_USERNAME
            + " TEXT," + COLUMN_FRIEND_LAT + " TEXT," + COLUMN_FRIEND_LNG + " TEXT,"
            + COLUMN_FRIEND_FIRSTNAME + " TEXT,"
            + COLUMN_FRIEND_LASTNAME + " TEXT,"
            + COLUMN_FRIEND_EMAIL + " TEXT" + ")";

    public friendLocationDB(Context context) {
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
