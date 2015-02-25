package com.coen268.invitenow.nishant.invitenowv20;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;


public class EditProfileActivity extends ActionBarActivity {

    private EditText firstNameEditText,lastNameEditText, emailEditText;
    private String firstName,lastName, email;
    String usernamePhone;
    String ParseObjID, usernameDB,passwordDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ImageView uploadPhoto = (ImageView) findViewById(R.id.profilePhotoBigEdit);
        uploadPhoto.setImageResource(R.drawable.uploadpic);

        firstNameEditText = (EditText)findViewById(R.id.FirstName);
        lastNameEditText = (EditText)findViewById(R.id.LastName);
        emailEditText = (EditText)findViewById(R.id.EmailID);

        readFromDB();
    }

    public void saveProfileChanges(View view) {

        firstName = firstNameEditText.getText().toString();
        lastName = lastNameEditText.getText().toString();
        email = emailEditText.getText().toString();


        if(firstName == null || lastName ==null || email ==null){


            Toast.makeText(getApplicationContext(), "Enter Information in Text",
                    Toast.LENGTH_SHORT).show();


            /*
            ParseObject InviteTopic = new ParseObject("UserDetails");
            InviteTopic.put("FirstName", firstName);
            InviteTopic.put("LastName", lastName);
            InviteTopic.put("Email", email);
            InviteTopic.put("UserID", usernamePhone);
            InviteTopic.saveInBackground();

            saveUserInfoToSQLite();
            */

        }
        else{
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

            Toast.makeText(getApplicationContext(), ParseObjID,
                    Toast.LENGTH_SHORT).show();

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


        //-----For Debug-----//
        // newValues.put(NotesDB.NAME_COLUMN, path);
        //newValues.put(NotesDB.FILE_PATH_COLUMN, caption);
        //-----For Debug-----//

        db.insert(userDB.DATABASE_TABLE, null, newValues);
        Toast.makeText(getApplicationContext(), "Saved in DataBase", Toast.LENGTH_SHORT).show();
    }

    private void readFromDB() {
        SQLiteDatabase db = new userDB(this).getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from  User", null);
        int number = cursor.getCount();
        cursor.moveToLast();
        usernamePhone = cursor.getString(cursor.getColumnIndex(userDB.COLUMN_USERNAME));
        passwordDB = cursor.getString(cursor.getColumnIndex(userDB.COLUMN_PASSWORD));
        ParseObjID = cursor.getString(cursor.getColumnIndex(userDB.COLUMN_PARSE_OBJECT_ID));

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
