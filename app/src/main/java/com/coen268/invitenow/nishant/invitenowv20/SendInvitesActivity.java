package com.coen268.invitenow.nishant.invitenowv20;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

import java.util.Calendar;
import java.util.List;


public class SendInvitesActivity extends ActionBarActivity {

    private TextView displayTime;
    private Button pickTime;
    private EditText topic;
    private Button inviteButton;
    private TextView status;

    private int pHour;
    private int pMinute;

    private String topic1;

    private Double parseTest;

    static final int TIME_DIALOG_ID = 0;

    private LocationManager locationManager;
    private String provider;
    private Location location;

Button smsButton;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invites);

        String getintent = getIntent().getStringExtra("abc");

     final String[] lines = getintent.toString().split("\n");

        Button smsButton = (Button)findViewById(R.id.sms);


        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendSMS(lines);
            }
        });

/*
        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, "nOjQbfKBEdY3A2rYAM5JmhPITjtO4A1DJeJq7iD1",
                "3LHhgD5smXqrZmkSVbjU4RWMsuDfrinANHjR3YU5");
*/
        /* Set User's Photo in Profile bar at top */

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        }
        else {
            System.out.println("Location not available");
        }


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

    }


    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());
    }

    public void inviteButtonClicked(View view)
    {
        // Enable Local Datastore.
        status.setText(topic1);
        Double lat = (double) (location.getLatitude());
        Double lng = (double) (location.getLongitude());

        ParseObject InviteTopic = new ParseObject("InviteTopic");
        InviteTopic.put("Lat", lat);
        InviteTopic.put("Lng", lng);

        InviteTopic.saveInBackground();

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
        Intent enterProfile = new Intent(this, ProfileActivity.class);
        startActivity(enterProfile);
    }


    public void sendSMS(String[] abc){

        String message = "This is group SMS";

        for(int i = 0 ;i< abc.length -1 ;i++){


            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(abc[i],null,message,null,null);

        }
    }

}
