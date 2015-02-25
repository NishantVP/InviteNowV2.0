package com.coen268.invitenow.nishant.invitenowv20;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;


public class EditProfileActivity extends ActionBarActivity {

    private static final int RESULT_GALLERY =0;
    private static final int CAMERA_PIC_REQUEST =1;
    Bitmap  imageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        final CharSequence myList[] = {"Gallery","Camera capture"};
        RelativeLayout rl;
        rl = (RelativeLayout)findViewById(R.id.myRL);

        ImageView uploadPhoto = (ImageView) findViewById(R.id.profilePhotoBigEdit);
        uploadPhoto.setImageResource(R.drawable.uploadpic);

        final AlertDialog.Builder cusDialog = new AlertDialog.Builder(this);
        cusDialog.setTitle("Choose from options belows");
        cusDialog.setSingleChoiceItems(myList,-1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                if(arg1 == 1){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_PIC_REQUEST);
                }
                else if(arg1 == 0){
                    Intent galleryIntent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent , RESULT_GALLERY );
                }
            }
        });


        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cusDialog.show();
            }
        });

    }

    public void enterSendInvites(View view) {
        Intent enterSendInvites = new Intent(this, SendInvitesActivity.class);
        startActivity(enterSendInvites);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == RESULT_OK) {

                imageData = (Bitmap) data.getExtras().get("data");
                ImageView image = (ImageView) findViewById(R.id.profilePhotoBigEdit);
                image.setImageBitmap(imageData);

                // Compressing the image in Byte Array.

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageData.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Intent in1 = new Intent(this, ProfileActivity.class);
                in1.putExtra("image",byteArray);

            } else if (resultCode == RESULT_CANCELED){

                Toast.makeText(EditProfileActivity.this,"Profile picture update cancelled",Toast.LENGTH_SHORT).show();
            }

        }
        else if (requestCode == RESULT_GALLERY){
            if (resultCode == RESULT_OK) {

     /*           imageData = (Bitmap) data.getExtras().get("data");
                ImageView image = (ImageView) findViewById(R.id.profilePhotoBigEdit);
                image.setImageBitmap(imageData);

                // Compressing the image in Byte Array.

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageData.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Intent in1 = new Intent(this, ProfileActivity.class);
                in1.putExtra("image",byteArray); */

            } else if (resultCode == RESULT_CANCELED){

                Toast.makeText(EditProfileActivity.this,"Profile picture update cancelled",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
