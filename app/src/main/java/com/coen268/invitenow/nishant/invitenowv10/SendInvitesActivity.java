package com.coen268.invitenow.nishant.invitenowv20;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class SendInvitesActivity extends ActionBarActivity {

    private TextView displayTime;
    private Button pickTime;

    private int pHour;
    private int pMinute;

    static final int TIME_DIALOG_ID = 0;


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

        /* Set User's Photo in Profile bar at top */
        ImageView usersPhoto = (ImageView) findViewById(R.id.usersPhotoImageView);
        usersPhoto.setImageResource(R.drawable.panda);

        ImageView usersAvailabilityColor = (ImageView) findViewById(R.id.usersAvailabilityStatusColor);
        usersAvailabilityColor.setImageResource(R.drawable.green);

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


}
