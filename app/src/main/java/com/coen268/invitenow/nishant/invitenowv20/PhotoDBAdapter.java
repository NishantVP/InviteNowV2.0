package com.coen268.invitenow.nishant.invitenowv20;

/**
 * Created by Nishant on 3/8/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class PhotoDBAdapter {

    PhotoDB photoDB;

    public  PhotoDBAdapter(Context context){
        photoDB = new PhotoDB(context);
    }

    public long insertData(String filePath){
        SQLiteDatabase db = photoDB.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(photoDB.PIC_FILEPATH, filePath);
        long id = db.insert(photoDB.TABLE_NAME, null , contentvalues);
        Log.d("abc--------->>>>","ROW INSERTED");
        return id ;
    }

}
