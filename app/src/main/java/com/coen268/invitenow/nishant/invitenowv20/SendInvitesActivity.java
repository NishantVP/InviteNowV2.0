package com.coen268.invitenow.nishant.invitenowv20;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.ContactsContract;
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
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseException;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class SendInvitesActivity extends ActionBarActivity  implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private TextView displayTime;
    private  TextView usernameTextView;
    private Button pickTime;
    private EditText topic;
    private Button inviteButton;
    private TextView status;

    private int pHour;
    private int pMinute;

    private String topic1;
    private String objectId;
    private Double parseTest;

    static final int TIME_DIALOG_ID = 0;

    private LocationManager locationManager;
    private String provider;
    private Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    Double lat,lng;

    String usernamePhone;
    String FirstName;
    String LastName;
    String firstItemId;

    Double friendLat,friendLng;
    String ffirstname,flastname,femail;
    String fusername= "6692378282";
    String chanelsubNumber;

    String read_Friend_lat1,read_Friend_lng1,read_Friend_username;
    Double read_Friend_lat,read_Friend_lng;

    String MessageToSend;
    String MessageTopic;
    String MeetTime;
    EditText messageWritten;

    List<String> name = new ArrayList<String>();
    List<String> phno = new ArrayList<String>();
    String[] PhoneNumbersArray = new String[500];

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    pHour = hourOfDay;
                    pMinute = minute;
                    updateDisplay();
                    displayToast();
                }
            };

    private void updateDisplay() {
        displayTime.setText(
                new StringBuilder()
                        .append(pad(pHour)).append(":")
                        .append(pad(pMinute)));
    }


    private void displayToast() {
        Toast.makeText(this, new StringBuilder().append("Time choosen is ").append
                (displayTime.getText()), Toast.LENGTH_SHORT).show();

    }


    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void readFromDB() {
        SQLiteDatabase db = new userDB(this).getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from  User", null);
        int number = cursor.getCount();
        cursor.moveToLast();
        usernamePhone = cursor.getString(cursor.getColumnIndex(userDB.COLUMN_USERNAME));
        objectId = cursor.getString(cursor.getColumnIndex(userDB.COLUMN_PARSE_OBJECT_ID));
        FirstName = cursor.getString(cursor.getColumnIndex(userDB.COLUMN_FIRSTNAME));
        LastName = cursor.getString(cursor.getColumnIndex(userDB.COLUMN_LASTNAME));
    }

    private void readFriendDB() {
        SQLiteDatabase db = new friendLocationDB(this).getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from  Friends", null);
        int number = cursor.getCount();
        cursor.moveToLast();
        read_Friend_username = cursor.getString(cursor.getColumnIndex(friendLocationDB.COLUMN_FRIEND_USERNAME));
        read_Friend_lat1 = cursor.getString(cursor.getColumnIndex(friendLocationDB.COLUMN_FRIEND_LAT));
        read_Friend_lng1 = cursor.getString(cursor.getColumnIndex(friendLocationDB.COLUMN_FRIEND_LNG));

        read_Friend_lat = Double.parseDouble(read_Friend_lat1);
        read_Friend_lng = Double.parseDouble(read_Friend_lng1);
       // LastName = cursor.getString(cursor.getColumnIndex(friendLocationDB.COLUMN_LASTNAME));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invites);

        messageWritten = (EditText)findViewById(R.id.meetSubject);
        getAllContacts(this.getContentResolver());
        //System.out.println(phno);

        for(int i=0;i<500;i++)
        {
            PhoneNumbersArray[i]=phno.get(0);
            phno.remove(0);
        }
        for(int i=0;i<500;i++)
        {
            //System.out.println(PhoneNumbersArray[i]);
        }

        for(int i=0;i<500;i++)
        {
            //PhoneNumbersArray[i].replace(" ","");
            //PhoneNumbersArray[i].replace("-","");
            //PhoneNumbersArray[i].replace("+","");
            PhoneNumbersArray[i]=PhoneNumbersArray[i].replaceAll("\\W","");

            if(PhoneNumbersArray[i].length()>10) {
                PhoneNumbersArray[i] = PhoneNumbersArray[i].substring(PhoneNumbersArray[i].length() - 10);
                System.out.println(PhoneNumbersArray[i]);
            }
        }


/*
        phno.toArray(PhoneNumbersArray);
        System.out.println("The Array - ");
        System.out.println(PhoneNumbersArray);
*/
/*
        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, "nOjQbfKBEdY3A2rYAM5JmhPITjtO4A1DJeJq7iD1",
                "3LHhgD5smXqrZmkSVbjU4RWMsuDfrinANHjR3YU5");
*/
        /* Set User's Photo in Profile bar at top */

        // Get the location manager
        buildGoogleApiClient();
        mGoogleApiClient.connect();

        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        //Criteria criteria = new Criteria();
        //provider = locationManager.getBestProvider(criteria, false);
        //location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        /*if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        }
        else {
            System.out.println("Location not available");
        }
        */

        ImageView usersPhoto = (ImageView) findViewById(R.id.usersPhotoImageView);
        usersPhoto.setImageResource(R.drawable.panda);

        ImageView usersAvailabilityColor = (ImageView) findViewById(R.id.usersAvailabilityStatusColor);
        usersAvailabilityColor.setImageResource(R.drawable.green);

        status = (TextView)findViewById(R.id.usersStatusTextView);
        /* Capture our View elements */
        displayTime = (TextView) findViewById(R.id.timeDisplay);
        pickTime = (Button) findViewById(R.id.timeOtherRadioButton);

        /* Listener for click event of the button */
        pickTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

        /* Get the current time */
        final Calendar cal = Calendar.getInstance();
        pHour = cal.get(Calendar.HOUR_OF_DAY);
        pMinute = cal.get(Calendar.MINUTE);

        /* Get text from edit text*/
        topic = (EditText) findViewById(R.id.meetSubject);
        topic1= topic.getText().toString();
       // status.setText(topic1);
        parseTest = 2.55;

        readFromDB();
        //objectId="hardset";
        //getFromParse();

        //Log.d("objID :" , objectId);
        status.setText(objectId);
        //writeUserIDtoParse();
        usernameTextView = (TextView)findViewById(R.id.usersNameTextView);
        usernameTextView.setText(FirstName +" " + LastName);
        writeLocationToParse();
        getNearbyLocationFromParse();

        /*Write channel for subscription*/
        chanelsubNumber = "ch"+usernamePhone;
        System.out.println(chanelsubNumber);
        ParsePush.subscribeInBackground(chanelsubNumber, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }

    public void saveFriendToSQLite(String fusername, String friendLat ,String friendLng,
                                   String ffirstname,String flastname, String femail ) {

        SQLiteDatabase db = new friendLocationDB(this).getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(friendLocationDB.COLUMN_FRIEND_USERNAME, fusername);
        newValues.put(friendLocationDB.COLUMN_FRIEND_LAT, friendLat);
        newValues.put(friendLocationDB.COLUMN_FRIEND_LNG, friendLng);
        newValues.put(friendLocationDB.COLUMN_FRIEND_FIRSTNAME, ffirstname);
        newValues.put(friendLocationDB.COLUMN_FRIEND_LASTNAME, flastname);
        newValues.put(friendLocationDB.COLUMN_FRIEND_EMAIL, femail);

        //-----For Debug-----//
        // newValues.put(NotesDB.NAME_COLUMN, path);
        //newValues.put(NotesDB.FILE_PATH_COLUMN, caption);
        //-----For Debug-----//

        db.insert(friendLocationDB.DATABASE_TABLE, null, newValues);

    }

    public void getFromParse()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
        query.whereEqualTo("UserID", "4085655184");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
                    firstItemId = scoreList.get(0).getObjectId();
                    //objectId=firstItemId;
                    //status.setText(firstItemId);
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

        /*
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
        query.whereEqualTo("UserID", "4085655184");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
        /*    public void done(ParseObject oResult, ParseException e) {
                if (e==null) {
                    // Object is Successfully Retrieved
                    objectId = oResult.getString("FirstName");
                } else {
                    // Problem with Retrieve
                    e.printStackTrace();
                }
            }
        });*/
        /*
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("score 1", "The getFirst request failed.");
                }
                else {
                    objectId = "Nishan";
                    Log.d("score 1", "Retrieved the object.");

                    //Firstname = object.getString("FirstName");
                    //Lastname = object.getString("LastName");
                    //EmailID = object.getString("Email");
                }
            }
        });
        */
    }

    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());
    }

    public void inviteButtonClicked(View view)
    {
        // Enable Local Datastore.
        final ParseObject InviteTopic = new ParseObject("InviteTopic");
        InviteTopic.put("Lat", lat);
        InviteTopic.put("Lng", lng);
        InviteTopic.put("UserID", usernamePhone);
        InviteTopic.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                // Now you can do whatever you want with the object ID, like save it in a variable
               // objectId = InviteTopic.getObjectId();
            }
        });
        //getNearbyLocationFromParse();
        readFriendDB();

        System.out.println("FLat " +read_Friend_lat);
        System.out.println("FLgn " +read_Friend_lng);

       // InviteTopic.saveInBackground();
        //status.setText(objectId);


        // Create our Installation query

        //JSONObject data = new JSONObject("{\"alert\": \"The Mets scored!\"}");

        MessageTopic = messageWritten.getText().toString();
        MeetTime = " in 15 min";
        MessageToSend = usernamePhone + ": " + MessageTopic + MeetTime;

       JSONObject data = new JSONObject();
        try {
            data.put("sender", usernamePhone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(data);

        ParseQuery pushQuery = ParseInstallation.getQuery();
                pushQuery.whereEqualTo("channels", "ch4085655175");

        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query

        //ParsePush push = new ParsePush();
        //push.setChannel("ch4085655175");
        push.setMessage(MessageToSend);

        //push.setData(data);
        push.sendInBackground();

    }

    public void writeLocationToParse()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
        // Retrieve the object by id
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject UserData, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    UserData.put("Lat", lat);
                    UserData.put("Lng", lng);
                    //UserData.put("LastName", lastName);
                    //UserData.put("Email", email);
                    UserData.saveInBackground();

                }
            }
        });
        Toast.makeText(this, "location written", Toast.LENGTH_SHORT).show();
    }

    public void getNearbyLocationFromParse()
    {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
        query.whereNotEqualTo("UserID", "unknown");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> FriendList, ParseException e) {
                if (e == null) {
                    Log.d("Friends", "Retrieved " + FriendList.size() + " Friends");

                    for(int i=0;i<FriendList.size();i++) {
                        friendLat = FriendList.get(i).getDouble("Lat");
                        friendLng = FriendList.get(i).getDouble("Lng");
                        ffirstname = FriendList.get(i).getString("FirstName");
                        flastname = FriendList.get(i).getString("LastName");
                        femail = FriendList.get(i).getString("Email");
                        fusername = FriendList.get(i).getString("UserID");

                        String friendLat1 = Double.toString(friendLat);
                        String friendLng1 = Double.toString(friendLng);
                        //firstItemId = FriendList.get(0).getObjectId();
                        //objectId=firstItemId;
                        //status.setText(firstItemId);
                        //System.out.println("FLat " +friendLat);
                        //System.out.println("FLgn " +friendLng);
                        double latDiff =  Math.abs (friendLat - lat);
                        double lngDiff =  Math.abs (friendLng - lng);
                        double distance = 1000*(latDiff+lngDiff);

                        if(distance < 2 )
                        {
                            System.out.println("Username : " +fusername);
                            saveFriendToSQLite(fusername, friendLat1, friendLng1,
                            ffirstname, flastname, femail);
                        }
                    }

                    Toast.makeText(getApplicationContext(), "Friend Saved", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

        /*
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
        // Retrieve the object by id
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject UserData, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    UserData.put("Lat", lat);
                    UserData.put("Lng", lng);
                    //UserData.put("LastName", lastName);
                    //UserData.put("Email", email);
                    UserData.saveInBackground();

                }
            }
        });
        Toast.makeText(this, "location written", Toast.LENGTH_SHORT).show();
        */
    }

    private void getAllContacts(ContentResolver contentResolver) {


        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        while(cursor.moveToNext()){
            String name1 = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phn = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Toast.makeText(SendInvitesActivity.this,"Phone number:"+phn,Toast.LENGTH_SHORT);
            /*if(!name.contains(name1)){

                name.add(name1);
                phno.add(phn);
            }*/
            if(!phno.contains(phn)){

                name.add(name1);
                phno.add(phn);
            }


        }
        cursor.close();
    }

    /* Create a new dialog for time picker */

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mTimeSetListener, pHour, pMinute, false);
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_invites, menu);
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

    public void enterSelectContacts(View view) {
        Intent enterSelectContacts = new Intent(this, SelectFromContactsActivity.class);
        startActivity(enterSelectContacts);
    }


    public void enterSelectGroups(View view) {
        Intent enterSelectGroups = new Intent(this, SelectFromGroupsActivity.class);
        startActivity(enterSelectGroups);
    }

    public void enterSettings(View view) {
        Intent enterSettings = new Intent(this, SettingsActivity.class);
        startActivity(enterSettings);
    }


    public void enterProfile(View view) {
        getNearbyLocationFromParse();
        Intent enterProfile = new Intent(this, ProfileActivity.class);
        startActivity(enterProfile);
    }


    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Toast.makeText(this, "location taken", Toast.LENGTH_SHORT).show();
            lat = (double) (mLastLocation.getLatitude());
            lng = (double) (mLastLocation.getLongitude());

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
