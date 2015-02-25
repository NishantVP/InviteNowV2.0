package com.coen268.invitenow.nishant.invitenowv20;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class ProfileActivity extends ActionBarActivity {

    ImageView usersPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
         usersPhoto = (ImageView) findViewById(R.id.profilePhotoBig);


        Button b1 = (Button)findViewById(R.id.updatebut);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decoder();
            }
        });


    }

    private void decoder() {

        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap imageData = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        usersPhoto.setImageBitmap(imageData);

    }

    public void enterEditProfile(View view) {
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
