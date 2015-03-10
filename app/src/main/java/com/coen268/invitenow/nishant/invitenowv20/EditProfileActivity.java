package com.coen268.invitenow.nishant.invitenowv20;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EditProfileActivity extends ActionBarActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText;
    private String firstName, lastName, email;
    String usernamePhone;
    String getUsernamePhone,getFirstName,getLastName,getEmaill;
    String ParseObjID, usernameDB, passwordDB;

    private static final int RESULT_GALLERY = 0;
    private static final int CAMERA_PIC_REQUEST = 1;
    Bitmap imageData;
    File photoFile = null;
    PhotoDBAdapter dbAdap = new PhotoDBAdapter(this);
    ImageView image;
    String imgFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final CharSequence myList[] = {"Gallery", "Camera capture"};
        RelativeLayout rl;
        rl = (RelativeLayout) findViewById(R.id.myRL);

        ImageView uploadPhoto = (ImageView) findViewById(R.id.profilePhotoBigEdit);
        uploadPhoto.setImageResource(R.drawable.uploadpic);

        firstNameEditText = (EditText) findViewById(R.id.FirstName);
        lastNameEditText = (EditText) findViewById(R.id.LastName);
        emailEditText = (EditText) findViewById(R.id.EmailID);

        readFromDB();

        firstNameEditText.setText(getFirstName);
        lastNameEditText.setText(getLastName);
        emailEditText.setText(getEmaill);

        final AlertDialog.Builder cusDialog = new AlertDialog.Builder(this);
        cusDialog.setTitle("Choose from options belows");
        cusDialog.setSingleChoiceItems(myList, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                if (arg1 == 1) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go

                        try {

                            photoFile = createImageFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (photoFile != null) {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);

                        } else {
                            //Toast.makeText(EditProfileActivity.this, "NO PHOTO FILE", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (arg1 == 0) {
                    Intent galleryIntent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_GALLERY);
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

    private void readFromDB() {
        SQLiteDatabase db = new userDB(this).getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from  User", null);
        int number = cursor.getCount();
        cursor.moveToLast();
        getUsernamePhone = cursor.getString(cursor.getColumnIndex(userDB.COLUMN_USERNAME));
        usernamePhone = getUsernamePhone;
        getEmaill = cursor.getString(cursor.getColumnIndex(userDB.COLUMN_EMAIL));
        getFirstName = cursor.getString(cursor.getColumnIndex(userDB.COLUMN_FIRSTNAME));
        getLastName = cursor.getString(cursor.getColumnIndex(userDB.COLUMN_LASTNAME));
        passwordDB = cursor.getString(cursor.getColumnIndex(userDB.COLUMN_PASSWORD));
        ParseObjID = cursor.getString(cursor.getColumnIndex(userDB.COLUMN_PARSE_OBJECT_ID));
        //System.out.println("Firstname : " +FirstName);
        //usernameTextView.setText(FirstName);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (photoFile.exists()) {
                    Toast.makeText(EditProfileActivity.this, "File saved at: " + photoFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    dbAdap.insertData(imgFilePath);

                } else {
                    Toast.makeText(EditProfileActivity.this, "File not saved", Toast.LENGTH_SHORT).show();
                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(EditProfileActivity.this, "Profile picture update cancelled", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == RESULT_GALLERY) {
            if (resultCode == RESULT_OK) {

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(EditProfileActivity.this, "Profile picture update cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //Toast.makeText(EditProfileActivity.this, "I am here", Toast.LENGTH_SHORT).show();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        imgFilePath = image.getAbsolutePath();
        Toast.makeText(EditProfileActivity.this, "Take Picture", Toast.LENGTH_SHORT).show();
        return image;

    }

    public void saveProfileChanges(View view) {

        firstName = firstNameEditText.getText().toString();
        lastName = lastNameEditText.getText().toString();
        email = emailEditText.getText().toString();


        if (firstName == null || lastName == null || email == null) {


            //Toast.makeText(getApplicationContext(), "Enter Information in Text",
              //      Toast.LENGTH_SHORT).show();


        } else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
            // Retrieve the object by id
            query.getInBackground(ParseObjID, new GetCallback<ParseObject>() {
                public void done(ParseObject UserData, ParseException e) {
                    if (e == null) {
                        // Now let's update it with some new data. In this case, only cheatMode and score
                        // will get sent to the Parse Cloud. playerName hasn't changed.
                        UserData.put("FirstName", firstName);
                        UserData.put("LastName", lastName);
                        UserData.put("Email", email);
                        UserData.saveInBackground();
                    }
                }
            });

            saveUserInfoToSQLite();

            Intent enterSendInvites = new Intent(this, SendInvitesActivity.class);
            startActivity(enterSendInvites);

            //Toast.makeText(getApplicationContext(), ParseObjID,
              //      Toast.LENGTH_SHORT).show();

        }
    }

    public void saveUserInfoToSQLite() {
        SQLiteDatabase db = new userDB(this).getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(userDB.COLUMN_FIRSTNAME, firstName);
        newValues.put(userDB.COLUMN_LASTNAME, lastName);
        newValues.put(userDB.COLUMN_EMAIL, email);
        newValues.put(userDB.COLUMN_USERNAME, usernamePhone);
        newValues.put(userDB.COLUMN_PASSWORD, passwordDB);
        newValues.put(userDB.COLUMN_PARSE_OBJECT_ID, ParseObjID);
        db.insert(userDB.DATABASE_TABLE, null, newValues);
        //Toast.makeText(getApplicationContext(), "Saved in DataBase", Toast.LENGTH_SHORT).show();
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
}
