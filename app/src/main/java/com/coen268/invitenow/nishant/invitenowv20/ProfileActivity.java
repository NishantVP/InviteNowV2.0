package com.coen268.invitenow.nishant.invitenowv20;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;


public class ProfileActivity extends ActionBarActivity {

    String filepath;

    File imgFile;
    ImageView usersPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usersPhoto = (ImageView)findViewById(R.id.profilePhotoBig);

        SQLiteDatabase db = new PhotoDB(this).getWritableDatabase();
        String[] result = {PhotoDB.UID,PhotoDB.PIC_FILEPATH};

        Cursor cursor = db.query(PhotoDB.TABLE_NAME,result,null,null,null,null,null);

        int numb = cursor.getCount();
        if(numb <= 0){

            usersPhoto.setImageResource(R.drawable.panda);
        }
        else{
            cursor.moveToLast();
            filepath = cursor.getString(1);
            imageLoad();
        }

    }

    public void imageLoad(){
        //Toast.makeText(ProfileActivity.this,"Path:" +filepath,Toast.LENGTH_SHORT).show();
        imgFile = new File(filepath);
        String str= imgFile.getAbsolutePath();
        Log.d("***** error***",str);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 5;
        Bitmap bitmap = BitmapFactory.decodeFile(str);
        usersPhoto.setImageBitmap(bitmap);
    }

    public void enterEditProfile2(View view) {
        Intent enterEditProfile = new Intent(this, EditProfileActivity.class);
        startActivity(enterEditProfile);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
