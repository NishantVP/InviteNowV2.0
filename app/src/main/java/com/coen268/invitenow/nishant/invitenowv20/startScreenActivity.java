package com.coen268.invitenow.nishant.invitenowv20;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParsePush;
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

    String ParseUserDataObjID = "ParseObjID";
    String Firstname = "Welcome";
    String Lastname = "To InviteNow";
    String EmailID = "myEmail";
    String firstItemId;

    final Context context = this;
    int LocationOnFlag = 0;

    Button LoginButton;
    Button SignUpButton;
    TextView LoginProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");

            // first time task
            saveUserToSQLite("sampleUser", "samplePassword");
            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }

        username = (EditText)findViewById(R.id.usernameEditText);
        password = (EditText)findViewById(R.id.passwordEditText);
        LoginButton = (Button)findViewById(R.id.loginButton);
        SignUpButton = (Button)findViewById(R.id.signUpButton);
        LoginProgress = (TextView)findViewById(R.id.LoginProgress);

        readFromDB();
        deleteall();
        deleteallRecipients();

        LocationManager lm = null;
        boolean gps_enabled = false, network_enabled = false;

        if(lm == null){

            lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        }
        try{

            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception e){

        }

        try{
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        }catch (Exception e){

        }

        if(!gps_enabled && !network_enabled){
            LocationOnFlag =1;

            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage(getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton("Location Services",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }

            });


            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LoginProgress.setText("Please turn on your location First");
                    dialog.cancel();

                }
            });

            dialog.show();
        }

        Boolean NoAutoLogin = false;
        NoAutoLogin = passwordCheck.contentEquals("samplePassword");

        if(NoAutoLogin==false)
        {
            username.setText(usernameCheck);
            password.setText("******");
            LoginButton.setEnabled(false);
            SignUpButton.setEnabled(false);
            LoginProgress.setText("Please wait ...");
        }


        if(LocationOnFlag==0) {
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
                        //Toast.makeText(getApplicationContext(), passwordCheck,
                        //        Toast.LENGTH_SHORT).show();
                        LoginButton.setEnabled(true);
                        SignUpButton.setEnabled(true);
                        LoginProgress.setText("Please Login / Sign up");
                    }
                }
            });
        }

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

    public void deleteall(){
        //SQLiteDatabase db = new userDB(this).getWritableDatabase();
        SQLiteDatabase db = new friendLocationDB(this).getWritableDatabase();
        //String delete = "TRUNCATE FROM tweets";
        //db.rawQuery(delete, null);
        db.delete("Friends",null,null);
        //Toast.makeText(getApplicationContext(), "Database Table cleared Deleted",
        //        Toast.LENGTH_SHORT).show();
    }

    public void deleteallRecipients(){
        //SQLiteDatabase db = new userDB(this).getWritableDatabase();
        SQLiteDatabase db = new recipientsDB(this).getWritableDatabase();
        //String delete = "TRUNCATE FROM tweets";
        //db.rawQuery(delete, null);
        db.delete("Recipients",null,null);
        //Toast.makeText(getApplicationContext(), "Recipients Cleared",
        //        Toast.LENGTH_SHORT).show();
    }

    public void getFromParse()
    {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
        query.whereEqualTo("UserID", usernameString);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> dataList, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + dataList.size() + " scores");
                    firstItemId = dataList.get(0).getObjectId();
                    Firstname = dataList.get(0).getString("FirstName");
                    Lastname = dataList.get(0).getString("LastName");
                    EmailID = dataList.get(0).getString("Email");

                    //objectId=firstItemId;
                    //status.setText(firstItemId);

                    saveUserToSQLiteOnLogin(usernameString, passwordString, firstItemId,Firstname);
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

        db.insert(userDB.DATABASE_TABLE, null, newValues);
        //Toast.makeText(getApplicationContext(), "Saved in DataBase", Toast.LENGTH_SHORT).show();
    }

    public void saveUserToSQLiteOnLogin(String usernameDB, String passwordDB, String ObjID,String Firstname) {
        SQLiteDatabase db = new userDB(this).getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(userDB.COLUMN_USERNAME, usernameDB);
        newValues.put(userDB.COLUMN_PASSWORD, passwordDB);
        newValues.put(userDB.COLUMN_PARSE_OBJECT_ID, ObjID);
        newValues.put(userDB.COLUMN_FIRSTNAME, Firstname);
        newValues.put(userDB.COLUMN_LASTNAME, Lastname);
        newValues.put(userDB.COLUMN_EMAIL, EmailID);

        db.insert(userDB.DATABASE_TABLE, null, newValues);
        //System.out.println("Firstname : " +Firstname);
        //Toast.makeText(getApplicationContext(), "Saved in DataBase Obj" +ObjID , Toast.LENGTH_SHORT).show();
    }


    private void readFromDB() {
        SQLiteDatabase db = new userDB(this).getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from  User", null);
        int number = cursor.getCount();
        if(number <= 0)
        {
            saveUserToSQLite("sampleUser", "samplePassword");
        }

        cursor.moveToLast();

        usernameCheck = cursor.getString(cursor.getColumnIndex(userDB.COLUMN_USERNAME));
        passwordCheck = cursor.getString(cursor.getColumnIndex(userDB.COLUMN_PASSWORD));

    }

    public void initiateParseUserData()
    {
        final ParseObject UserData = new ParseObject("UserData");
        UserData.put("FirstName", "Weocome to");
        UserData.put("LastName", "InviteNow");
        UserData.put("Email", "email");
        UserData.put("UserID", "userID");

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