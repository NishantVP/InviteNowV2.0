package com.coen268.invitenow.nishant.invitenowv20;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ParseQuery;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.List;


public class startScreenActivity extends ActionBarActivity {

    EditText username, password;
    Button signUp;
    String usernameString, passwordString;
    String usernameCheck;
    String passwordCheck;
    int clickedUserID;
    boolean tableInMemory;

    String ParseUserDataObjID = "UNKNOWN";
    String Firstname = "UNKNOWN";
    String Lastname = "UNKNOWN";
    String EmailID = "UNKNOWN";
    String firstItemId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        username = (EditText)findViewById(R.id.usernameEditText);
        password = (EditText)findViewById(R.id.passwordEditText);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "nOjQbfKBEdY3A2rYAM5JmhPITjtO4A1DJeJq7iD1",
                "3LHhgD5smXqrZmkSVbjU4RWMsuDfrinANHjR3YU5");
        //ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access by default.
        // defaultACL.setPublicReadAccess(true);
        //ParseACL.setDefaultACL(defaultACL, true);
        //ParseUser.enableAutomaticUser();

        /*
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar1");
        testObject.saveInBackground();
        */
/*
        SQLiteDatabase db = new userDB(this).getWritableDatabase();
        tableInMemory = isTableExists(db,userDB.DATABASE_TABLE);
        if(!tableInMemory) {
            saveUserToSQLite("sample user", "sample password");
        }*/

        readFromDB();

        ParseUser.logInInBackground(usernameCheck, passwordCheck, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    Toast.makeText(getApplicationContext(), "Login Success",
                            Toast.LENGTH_SHORT).show();

                    Intent enterSendInvites2 = new Intent(startScreenActivity.this, SendInvitesActivity.class);
                    startActivity(enterSendInvites2);

                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    Toast.makeText(getApplicationContext(), passwordCheck,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

       /* ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("username", usernameCheck);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    //Log.d("score", "The getFirst request failed.");
                    Toast.makeText(getApplicationContext(), usernameCheck,
                            Toast.LENGTH_SHORT).show();
                } else {
                    //Log.d("score", "Retrieved the object.");
                    Toast.makeText(getApplicationContext(), "Login Success",
                            Toast.LENGTH_SHORT).show();
                    Intent enterSendInvites2 = new Intent(startScreenActivity.this, SendInvitesActivity.class);
                    startActivity(enterSendInvites2);
                }
            }
        });
        */
        /*
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
        */

    }

    boolean isTableExists(SQLiteDatabase db, String tableName)
    {
        if (tableName == null || db == null || !db.isOpen())
        {
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
        if (!cursor.moveToFirst())
        {
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public void doLogin(View view) {
        usernameString = username.getText().toString();
        passwordString = password.getText().toString();

        ParseUser.logInInBackground(usernameString, passwordString, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.

                    /*
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
                    query.whereEqualTo("UserID", "4085655184");
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            if (object == null) {
                                Log.d("score", "The getFirst request failed.");
                            } else {
                                Log.d("score", "Retrieved the object.");
                                ParseUserDataObjID = object.getString("objectId");
                                Firstname = object.getString("FirstName");
                                Lastname = object.getString("LastName");
                                EmailID = object.getString("Email");
                            }
                        }
                    });
                    */
                    getFromParse();
                    //saveUserToSQLite(usernameString, passwordString);

                    Toast.makeText(getApplicationContext(), Firstname,
                            Toast.LENGTH_SHORT).show();

                    Intent enterSendInvites2 = new Intent(startScreenActivity.this, SendInvitesActivity.class);
                    startActivity(enterSendInvites2);

                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    Toast.makeText(getApplicationContext(), "Username or Password incorrect",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getFromParse()
    {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
        query.whereEqualTo("UserID", usernameString);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
                    firstItemId = scoreList.get(0).getObjectId();
                    //objectId=firstItemId;
                    //status.setText(firstItemId);
                    saveUserToSQLiteOnLogin(usernameString, passwordString, firstItemId);
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });


    }

    public void doSignUp(View view)
    {
        initiateParseUserData();

        usernameString = username.getText().toString();
        passwordString = password.getText().toString();

        if(usernameString != null && passwordString != null) {
            ParseUser user = new ParseUser();
            user.setUsername(usernameString);
            user.setPassword(passwordString);
            //user.setEmail("email@example.com");

            // other fields can be set just like with ParseObject
            //user.put("phone", "650-253-0000");

            user.signUpInBackground(new com.parse.SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // Hooray! Let them use the app now.
                        Toast.makeText(getApplicationContext(), "Sign Up Successful",
                                Toast.LENGTH_SHORT).show();

                        saveUserToSQLite(usernameString,passwordString);

                        writeUserIDtoParse();
                        Intent enterEditProfile = new Intent(startScreenActivity.this, EditProfileActivity.class);
                        startActivity(enterEditProfile);


                    } else {
                        // Sign up didn't succeed. Look at the ParseException
                        // to figure out what went wrong
                        Toast.makeText(getApplicationContext(), "Something went wrong",
                                Toast.LENGTH_SHORT).show();
                        String e1= e.toString();
                        Log.d("Parse Exception",e1);


                    }
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Something went wrong 2",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void saveUserToSQLite(String usernameDB, String passwordDB) {
        SQLiteDatabase db = new userDB(this).getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(userDB.COLUMN_USERNAME, usernameDB);
        newValues.put(userDB.COLUMN_PASSWORD, passwordDB);
        newValues.put(userDB.COLUMN_PARSE_OBJECT_ID, ParseUserDataObjID);
        newValues.put(userDB.COLUMN_FIRSTNAME, Firstname);
        newValues.put(userDB.COLUMN_LASTNAME, Lastname);
        newValues.put(userDB.COLUMN_EMAIL, EmailID);

        //-----For Debug-----//
        // newValues.put(NotesDB.NAME_COLUMN, path);
        //newValues.put(NotesDB.FILE_PATH_COLUMN, caption);
        //-----For Debug-----//

        db.insert(userDB.DATABASE_TABLE, null, newValues);
        Toast.makeText(getApplicationContext(), "Saved in DataBase", Toast.LENGTH_SHORT).show();
    }

    public void saveUserToSQLiteOnLogin(String usernameDB, String passwordDB, String ObjID) {
        SQLiteDatabase db = new userDB(this).getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(userDB.COLUMN_USERNAME, usernameDB);
        newValues.put(userDB.COLUMN_PASSWORD, passwordDB);
        newValues.put(userDB.COLUMN_PARSE_OBJECT_ID, ObjID);
        newValues.put(userDB.COLUMN_FIRSTNAME, Firstname);
        newValues.put(userDB.COLUMN_LASTNAME, Lastname);
        newValues.put(userDB.COLUMN_EMAIL, EmailID);

        //-----For Debug-----//
        // newValues.put(NotesDB.NAME_COLUMN, path);
        //newValues.put(NotesDB.FILE_PATH_COLUMN, caption);
        //-----For Debug-----//

        db.insert(userDB.DATABASE_TABLE, null, newValues);
        Toast.makeText(getApplicationContext(), "Saved in DataBase Obj" +ObjID , Toast.LENGTH_SHORT).show();
    }


    private void readFromDB() {
        SQLiteDatabase db = new userDB(this).getWritableDatabase();
        /*
        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        String[] resultColumns = {userDB.COLUMN_ID, userDB.COLUMN_USERNAME, userDB.COLUMN_PASSWORD};
        Cursor cursor = db.query(userDB.DATABASE_TABLE, resultColumns, where, whereArgs,
                groupBy, having, order);
        while (cursor.moveToNext()) {
            int idInDB = cursor.getInt(cursor.getColumnIndex(userDB.COLUMN_ID));
            if (clickedUserID + 1 == idInDB) {
                usernameCheck = cursor.getString(cursor.getColumnIndex(userDB.COLUMN_USERNAME));
                passwordCheck = cursor.getString(cursor.getColumnIndex(userDB.COLUMN_PASSWORD));
                break;
            }
        }
        */
        Cursor cursor = db.rawQuery("Select * from  User", null);
        int number = cursor.getCount();
        if(number == 0)
        {
            saveUserToSQLite("sample user", "sample password");
        }

        cursor.moveToLast();
        usernameCheck = cursor.getString(cursor.getColumnIndex(userDB.COLUMN_USERNAME));
        passwordCheck = cursor.getString(cursor.getColumnIndex(userDB.COLUMN_PASSWORD));

    }

    public void initiateParseUserData()
    {
        final ParseObject UserData = new ParseObject("UserData");
        UserData.put("FirstName", "unknown");
        UserData.put("LastName", "unknown");
        UserData.put("Email", "unknown");
        UserData.put("UserID", "unknown");

        UserData.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                // Now you can do whatever you want with the object ID, like save it in a variable
                ParseUserDataObjID = UserData.getObjectId();
            }
        });
    }

    public void writeUserIDtoParse()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
        // Retrieve the object by id
        query.getInBackground(ParseUserDataObjID, new GetCallback<ParseObject>() {
            public void done(ParseObject UserData, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    UserData.put("UserID", usernameString);
                    //UserData.put("LastName", lastName);
                    //UserData.put("Email", email);
                    UserData.saveInBackground();
                }
            }
        });
    }

    public void enterApplication(View view)
    {
        //Intent enterSendInvites = new Intent(this, SendInvitesActivity.class);
        //startActivity(enterSendInvites);

/*      Ability to change the Start Screen Image on tap. Random image is set at this moment.
        ImageView startScreenImage = (ImageView) findViewById(R.id.startScreenImage);
        startScreenImage.setImageResource(R.drawable.elephant);
*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_screen, menu);
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