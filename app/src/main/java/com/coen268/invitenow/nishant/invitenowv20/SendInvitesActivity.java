package com.coen268.invitenow.nishant.invitenowv20;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
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
import android.widget.RadioButton;
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
import com.parse.codec.binary.StringUtils;

import android.telephony.SmsManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
    Double lat = 0.0;
    Double lng = 0.0;

    String usernamePhone;
    String FirstName;
    String LastName;
    String firstItemId;

    Double friendLat,friendLng;
    String ffirstname,flastname,femail;
    String fusername= "6692378282";
    String chanelsubNumber;

    String MessageToSend;
    String MessageTopic;
    String MeetTime;
    EditText messageWritten;

    List<String> name = new ArrayList<String>();
    List<String> phno = new ArrayList<String>();
    List<String> PhoneNumbersProcessed = new ArrayList<String>();

    ArrayList<String> recipientsPhoneList = new ArrayList<>();
    ArrayList<String> recipientsFirstNameList = new ArrayList<>();
    ArrayList<String> recipientsLastNameList = new ArrayList<>();

    String[] RecipientsPhoneNumbers = new String[100];
    String[] RecipientsToApp = new String[100];
    String[] RecipientsToSMS = new String[100];
    ArrayList<String> friendListParse = new ArrayList<>();
    int NumberToSMS;
    int NumberToApp;

    RadioButton rb1,rb2,rb3,rb4,rb5;
    Boolean flag = false;

    TextView TestingTextView;
    int invtesSentFlag =0;

    String filepath;
    ImageView usersPhoto;
    File imgFile;

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
        //System.out.println("Firstname : " +FirstName);
        //usernameTextView.setText(FirstName);
    }


    public void readRecipientsDB() {
        SQLiteDatabase db = new recipientsDB(this).getWritableDatabase();
        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        String[] resultColumns = {recipientsDB.COLUMN_ID, recipientsDB.COLUMN_RECIPIENT_USERNAME,
                recipientsDB.COLUMN_RECIPIENT_FIRSTNAME, recipientsDB.COLUMN_RECIPIENT_LASTNAME};

        Cursor cursor = db.query(recipientsDB.DATABASE_TABLE, resultColumns, where, whereArgs,
                groupBy, having, order);

        int i=0;
        while (cursor.moveToNext()) {
            int idInDB = cursor.getInt(cursor.getColumnIndex(recipientsDB.COLUMN_ID));

            String username = cursor.getString(cursor.getColumnIndex(recipientsDB.COLUMN_RECIPIENT_USERNAME));
            String firstname = cursor.getString(cursor.getColumnIndex(recipientsDB.COLUMN_RECIPIENT_FIRSTNAME));
            String lastname = cursor.getString(cursor.getColumnIndex(recipientsDB.COLUMN_RECIPIENT_LASTNAME));

            if(!recipientsPhoneList.contains(username)) {
                //RecipientsPhoneNumbers[i]=username;
                //RecipientsFirstNames[i]=firstname;
                //RecipientsLastNames[i]=lastname;

                recipientsPhoneList.add(username);
                recipientsFirstNameList.add(username);
                recipientsLastNameList.add(username);

                i++;
                //System.out.println();
                //System.out.println("Array: " +RecipientsPhoneNumbers[i]);
            }
        }
        //System.out.println("List: " +recipientsPhoneList);
    }

    public void readFriendFromDB() {
        SQLiteDatabase db = new friendLocationDB(this).getWritableDatabase();
        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        String[] resultColumns = {friendLocationDB.COLUMN_ID, friendLocationDB.COLUMN_FRIEND_USERNAME,
                friendLocationDB.COLUMN_FRIEND_LAT, friendLocationDB.COLUMN_FRIEND_LNG,
                friendLocationDB.COLUMN_FRIEND_FIRSTNAME, friendLocationDB.COLUMN_FRIEND_LASTNAME,
                friendLocationDB.COLUMN_FRIEND_EMAIL};

        Cursor cursor = db.query(friendLocationDB.DATABASE_TABLE, resultColumns, where, whereArgs,
                groupBy, having, order);

        int i=0;
        while (cursor.moveToNext()) {
            int idInDB = cursor.getInt(cursor.getColumnIndex(friendLocationDB.COLUMN_ID));

            String username = cursor.getString(cursor.getColumnIndex(friendLocationDB.COLUMN_FRIEND_USERNAME));
            String firstname = cursor.getString(cursor.getColumnIndex(friendLocationDB.COLUMN_FRIEND_FIRSTNAME));
            String lastname = cursor.getString(cursor.getColumnIndex(friendLocationDB.COLUMN_FRIEND_LASTNAME));

            if(!friendListParse.contains(username)) {
                friendListParse.add(username);
                i++;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invites);

        messageWritten = (EditText)findViewById(R.id.meetSubject);
        TestingTextView = (TextView) findViewById(R.id.testingTextView);
        usernameTextView = (TextView)findViewById(R.id.usersNameTextView);

        processPhoneNumbers();
        readFriendFromDB();

        invtesSentFlag = 0;
        // Get the location manager
        buildGoogleApiClient();
        mGoogleApiClient.connect();

        rb1 = (RadioButton)findViewById(R.id.time5MinRadioButton);
        rb2 = (RadioButton)findViewById(R.id.time15MinRadioButton);
        rb3 = (RadioButton)findViewById(R.id.time30MinRadioButton);
        rb4 = (RadioButton)findViewById(R.id.time60MinRadioButton);
        rb5 = (RadioButton)findViewById(R.id.timeOtherRadioButton);


        rb1.setOnClickListener(new View.OnClickListener() {         // Radio button for 5min
            @Override
            public void onClick(View v) {


                rb2.setChecked(false);
                rb3.setChecked(false);
                rb4.setChecked(false);
                rb5.setChecked(false);

            }
        });

        rb2.setOnClickListener(new View.OnClickListener() {         // Radio button for 15min
            @Override
            public void onClick(View v) {


                rb1.setChecked(false);
                rb3.setChecked(false);
                rb4.setChecked(false);
                rb5.setChecked(false);

            }
        });

        rb3.setOnClickListener(new View.OnClickListener() {         // Radio button for 30min
            @Override
            public void onClick(View v) {


                rb1.setChecked(false);
                rb2.setChecked(false);
                rb4.setChecked(false);
                rb5.setChecked(false);

            }
        });

        rb4.setOnClickListener(new View.OnClickListener() {         // Radio button for 60min
            @Override
            public void onClick(View v) {


                rb1.setChecked(false);
                rb2.setChecked(false);
                rb3.setChecked(false);
                rb5.setChecked(false);
            }
        });

        rb5.setOnClickListener(new View.OnClickListener() {         // Radio button for any time
            @Override
            public void onClick(View v) {

                rb1.setChecked(false);
                rb2.setChecked(false);
                rb3.setChecked(false);
                rb4.setChecked(false);
            }
        });

        usersPhoto = (ImageView) findViewById(R.id.usersPhotoImageView);
        usersPhoto.setImageResource(R.drawable.panda);

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

                rb1.setChecked(false);
                rb2.setChecked(false);
                rb3.setChecked(false);
                rb4.setChecked(false);
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
        Boolean NoObjID = false;
        NoObjID = objectId.contentEquals("ParseObjID");

        if(NoObjID==true)
        {
            status.setText("Touch to update");
        }
        else
        {
            status.setText(usernamePhone);
        }

        //writeUserIDtoParse();

        usernameTextView.setText(FirstName +" " + LastName);
        writeLocationToParse();
        getNearbyLocationFromParse();

        /*Write channel for subscription*/
        chanelsubNumber = "ch"+usernamePhone;
        //System.out.println(chanelsubNumber);
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


    public void imageLoad(){

        //Toast.makeText(SendInvitesActivity.this,"Path:" +filepath,Toast.LENGTH_SHORT).show();
        imgFile = new File(filepath);
        String str= imgFile.getAbsolutePath();
        Log.d("***** error***",str);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 5;
        Bitmap bitmap = BitmapFactory.decodeFile(str);
        usersPhoto.setImageBitmap(bitmap);
    }

    public void processPhoneNumbers()
    {

        getAllContacts(this.getContentResolver());
        //System.out.println(phno);

        final int totalNumbers = phno.size();
        String[] PhoneNumbersArray = new String[totalNumbers];
        for(int i=0;i<totalNumbers;i++)
        {
            PhoneNumbersArray[i]=phno.get(0);
            phno.remove(0);
            PhoneNumbersArray[i]=PhoneNumbersArray[i].replaceAll("\\W","");

            if(PhoneNumbersArray[i].length()>10)
            {
                PhoneNumbersArray[i] = PhoneNumbersArray[i].substring(PhoneNumbersArray[i].length() - 10);
            }

        }
        PhoneNumbersProcessed = Arrays.asList(PhoneNumbersArray);
        //System.out.println(totalNumbers);
        //System.out.println(PhoneNumbersProcessed.get(PhoneNumbersProcessed.size()-1));

    }



    public void processRecipients()
    {
        NumberToApp=0;
        NumberToSMS=0;
        final int totalNumbers = recipientsPhoneList.size();
        String[] RecipientsPhoneNumbers_clean = new String[totalNumbers];
        int toAppCounter = 0;
        int toSMSCounter = 0;

        readFriendFromDB();
        for(int i=0;i<totalNumbers;i++)
        {
            RecipientsPhoneNumbers[i]=recipientsPhoneList.get(0);
            recipientsPhoneList.remove(0);

            RecipientsPhoneNumbers_clean[i]=RecipientsPhoneNumbers[i].replaceAll("\\W","");

            if(RecipientsPhoneNumbers_clean[i].length()>10)
            {
                RecipientsPhoneNumbers_clean[i] =
                        RecipientsPhoneNumbers_clean[i].substring(RecipientsPhoneNumbers_clean[i].length() - 10);
            }
            boolean friendinParseFlag = friendListParse.contains(RecipientsPhoneNumbers_clean[i]);
            //System.out.println("List-" +friendListParse);
            //System.out.println("Number-" +RecipientsPhoneNumbers_clean[i]);
            //System.out.println("Flag-" +friendinParseFlag);
            if(friendinParseFlag==true)
            {
                RecipientsToApp[toAppCounter]="ch"+ RecipientsPhoneNumbers_clean[i];
                //System.out.println("To App:" +RecipientsToApp[toAppCounter]  + " " +i);
                NumberToApp++;
                toAppCounter++;
                //RecipientsViaApp = RecipientsViaApp + "," +RecipientsToApp[toAppCounter];

            }
            else if(friendinParseFlag==false)
            {
                RecipientsToSMS[toSMSCounter]=RecipientsPhoneNumbers[i];
                //System.out.println("To SMS:" +RecipientsToSMS[toSMSCounter] + " " +i);
                NumberToSMS++;
                toSMSCounter++;
                //RecipientsViaSMS = RecipientsToSMS + "," + RecipientsToSMS[toSMSCounter];
            }
        }
        if(NumberToApp!=0 || NumberToSMS !=0)
        {
            invtesSentFlag = 0;
        }

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

        db.insert(friendLocationDB.DATABASE_TABLE, null, newValues);
    }



    public void doTesting(View view) {
        deleteallRecipients();
        TestingTextView.setText("Recipients-");
        readRecipientsDB();
        processRecipients();

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


    }

    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());
    }

    public void inviteButtonClicked(View view)
    {
        int messageSavedFlag = 0;

        // Create our Installation query

        //JSONObject data = new JSONObject("{\"alert\": \"The Mets scored!\"}");
        readRecipientsDB();
        processRecipients();


        MessageTopic = messageWritten.getText().toString();

        if(rb1.isChecked()){
            MeetTime = " in 5 min";
        }
        else if(rb2.isChecked()){
            MeetTime = " in 15 min";
        }
        else if(rb3.isChecked()){
            MeetTime = " in 30 min";
        }
        else if(rb4.isChecked()){
            MeetTime = " in 60 min";
        }
        else if(rb5.isChecked()){
            MeetTime = " at " + pHour + ":" + pMinute;
        }
        else{
            MeetTime=" ";
            //Toast.makeText(SendInvitesActivity.this,"Please select the time ",Toast.LENGTH_SHORT).show();
        }

        //MeetTime = " in 15 min";
        MessageToSend = usernamePhone + ": " + MessageTopic + MeetTime;

       /*
       JSONObject data = new JSONObject();
        try {
            data.put("sender", usernamePhone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(data);
        */

        //System.out.println("App " +NumberToApp +"SMS " +NumberToSMS);

        String sentToastToApp = Integer.toString(NumberToApp);
        String sentToastToSMS = Integer.toString(NumberToSMS);

        boolean zeroRecipients = false;

        if(NumberToApp==0&&NumberToSMS==0)
        {
            zeroRecipients=true;
        }
        if(invtesSentFlag==1 || zeroRecipients==true)
        {
            Toast.makeText(this, "Select Recipients ", Toast.LENGTH_SHORT).show();
        }

        int i;
        if(invtesSentFlag==0)
        {
            for (i = 0; i < NumberToApp; i++) {

                ParseQuery pushQuery = ParseInstallation.getQuery();
                pushQuery.whereEqualTo("channels", RecipientsToApp[i]);
                // Send push notification to query
                ParsePush push = new ParsePush();
                push.setQuery(pushQuery); // Set our Installation query
                //ParsePush push = new ParsePush();
                //push.setChannel("ch4085655175");
                push.setMessage(MessageToSend);
                //push.setData(data);
                push.sendInBackground();
            }
            if (i == NumberToApp && i!=0) {
                Toast.makeText(this, "Sending.. " + sentToastToApp + " via App", Toast.LENGTH_SHORT).show();
                NumberToApp = 0;
                if(messageSavedFlag==0)
                {
                    saveMessageToSQLite(MessageToSend);
                    messageSavedFlag=1;
                }
            }
            TestingTextView.setText("Recipients-");
        }


        if(NumberToSMS>0) {

            if(messageSavedFlag==0)
            {
                saveMessageToSQLite(MessageToSend);
                messageSavedFlag=1;
            }
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            final String sentToastToSMSdialog = Integer.toString(NumberToSMS);
            dialog.setMessage(sentToastToSMSdialog +" invitations will be sent by SMS");
            dialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int i;
                    if(invtesSentFlag==0) {

                        for (i = 0; i < NumberToSMS; i++) {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(RecipientsToSMS[i], null, MessageToSend, null, null);
                            //Toast.makeText(NearbyFriendsActivity.this, "message sent to: " + phoneNumbers[p],
                            //       Toast.LENGTH_SHORT).show();

                        }

                        if (i == NumberToSMS && i !=0) {
                            Toast.makeText(SendInvitesActivity.this, "Sending.. "
                                    + sentToastToSMSdialog + " via SMS", Toast.LENGTH_SHORT).show();
                            NumberToSMS = 0;

                        }
                        deleteallRecipients();
                    }
                }

            });

            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    NumberToSMS = 0;
                    deleteallRecipients();
                    Toast.makeText(SendInvitesActivity.this, "SMS not sent", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            });

            dialog.show();

        }
        //System.out.println("Number to SMS" +NumberToSMS);

        if(NumberToSMS==0)
        {
            //System.out.println("Recipients Deleted");
            invtesSentFlag=1;
            deleteallRecipients();
        }

    }


    public void saveMessageToSQLite(String message) {

        SQLiteDatabase db = new invitesDB(this).getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(invitesDB.COLUMN_MESSAGE, message);

        db.insert(invitesDB.DATABASE_TABLE, null, newValues);

    }

    public void writeLocationToParse()
    {
        if(lat != 0 && lng != 0) {
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
            //Toast.makeText(this, "location written", Toast.LENGTH_SHORT).show();
        }

        String LatitudeforTesting = Double.toString(lat);
        String LongitudeforTesting = Double.toString(lng);
        //TestingTextView.setText(LatitudeforTesting +"," +LongitudeforTesting);

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

                        double latDiff =  Math.abs (friendLat - lat);
                        double lngDiff =  Math.abs (friendLng - lng);
                        double distance = 1000*(latDiff+lngDiff);

                        boolean inContactsFlag = PhoneNumbersProcessed.contains(fusername);


                        if(distance < 5 && inContactsFlag == true)
                        {
                            //System.out.println("User In Contacts : " +fusername);
                            saveFriendToSQLite(fusername, friendLat1, friendLng1,
                            ffirstname, flastname, femail);
                        }
                    }

                    //Toast.makeText(getApplicationContext(), "Friend Saved", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });


    }

    private void getAllContacts(ContentResolver contentResolver) {


        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        while(cursor.moveToNext()){
            String name1 = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phn = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            //Toast.makeText(SendInvitesActivity.this,"Phone number:"+phn,Toast.LENGTH_SHORT);

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

    public void deleteallRecipients(){
        //SQLiteDatabase db = new userDB(this).getWritableDatabase();
        SQLiteDatabase db = new recipientsDB(this).getWritableDatabase();
        //String delete = "TRUNCATE FROM tweets";
        //db.rawQuery(delete, null);
        db.delete("Recipients",null,null);
        Toast.makeText(getApplicationContext(), "Recipients Cleared",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_invites, menu);
        return true;
    }

    @Override
    protected void onResume ()
    {
        //Toast.makeText(this, "On Resume Called", Toast.LENGTH_SHORT).show();
        TestingTextView.setText("Recipients-");
        super.onResume();
        readRecipientsDB();
        processRecipients();

        String[] RecipientsToApp2 = new String[100];
        for (int i = 0; i < NumberToApp; i++){
            RecipientsToApp2[i] = RecipientsToApp[i].substring(RecipientsToApp[i].length() - 10);
        }


        ArrayList<String> list1=new ArrayList<String>(Arrays.asList(RecipientsToApp2));
        ArrayList<String> list2=new ArrayList<String>(Arrays.asList(RecipientsToSMS));

        //System.out.println("RecipientsViaApp " +list1);

        if(NumberToApp!=0) {
            TestingTextView.append("\nVia APP: ");
        }
        for (int j = 0; j < NumberToApp; j++){
            TestingTextView.append( " " +list1.get(j));
        }

        if(NumberToSMS!=0) {
            TestingTextView.append("\nVia SMS: ");
        }
        for (int j = 0; j < NumberToSMS; j++){
            TestingTextView.append(" " +list2.get(j));
        }

    }
    @Override
    protected void onStop() {


        super.onStop();
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
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


    public void enterSelectNearbyFriends(View view) {
        Intent enterSelectNearbyFriends = new Intent(this, NearbyFriendsActivity.class);
        startActivity(enterSelectNearbyFriends);
    }

    public void enterSettings(View view) {
        Intent enterSettings = new Intent(this, SettingsActivity.class);
        startActivity(enterSettings);
    }


    public void enterEditProfile(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            recreate();
        }
        getNearbyLocationFromParse();
        Intent enterEditProfile = new Intent(this, EditProfileActivity.class);
        startActivity(enterEditProfile);
    }



    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //Toast.makeText(this, "location taken", Toast.LENGTH_SHORT).show();
            lat = (double) (mLastLocation.getLatitude());
            lng = (double) (mLastLocation.getLongitude());
            //Toast.makeText(this, "Connected to Google", Toast.LENGTH_SHORT).show();
            writeLocationToParse();

        }

    }

    @Override
    public void onBackPressed()
    {
        System.exit(0);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
