package com.coen268.invitenow.nishant.invitenowv20;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class SettingsActivity extends ActionBarActivity {

    private String[] Menus;

    private ListView menuList;
    private settingsMenuListAdapter menuListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        Menus = new String[] {"Edit Profile", " Notifications"," Location Settings"," Groups",
                "Preferences","View Messages"};
        menuList = (ListView) findViewById(R.id.listView);
        menuListAdapter = new settingsMenuListAdapter(this, Menus);

        menuList.setAdapter(menuListAdapter);


        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:

                        Intent enterEditProfile = new Intent(SettingsActivity.this, EditProfileActivity.class);
                        startActivity(enterEditProfile);

                        break;

                    case 1:
                        Intent enterNotifications = new Intent(SettingsActivity.this, NotificationActivity.class);
                        startActivity(enterNotifications);
                        break;


                    case 2:
                        Intent enterLocations = new Intent(SettingsActivity.this, LocationPreferenceActivity.class);
                        startActivity(enterLocations);
                        break;


                    case 3:
                        Intent enterGroups = new Intent(SettingsActivity.this, GroupsActivity.class);
                        startActivity(enterGroups);

                        break;


                    case 4:
                        Intent enterPreferences = new Intent(SettingsActivity.this, PreferencesActivity.class);
                        startActivity(enterPreferences);

                        break;

                    case 5:
                        Intent enterViewInvites = new Intent(SettingsActivity.this, ViewInvitesActivity.class);
                        startActivity(enterViewInvites);

                        break;
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
