package com.coen268.invitenow.nishant.invitenowv20;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.io.File;
import java.io.FileOutputStream;


public class SelectFromContactsActivity extends ActionBarActivity {

    private String[] contactNames;
    private int[] contactImages;
    private ListView contactList;
    private contactsListAdapter contactListAdapter;

/*
    public void initializeList()
    {
        contactImages = new int[] {R.drawable.carl,R.drawable.einstein,R.drawable.newton,
                R.drawable.tesla};
        contactNames = new String[] {" Carl Sagan"," Albert Einstein", " Isaac Newton"," Nikola Tesla"};
        contactList = (ListView) findViewById(R.id.lst_contacts);
        contactListAdapter = new contactsListAdapter(this, contactNames, contactImages);
        contactList.setAdapter(contactListAdapter);
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_from_contacts);


        contactImages = new int[] {R.drawable.carl,R.drawable.einstein,R.drawable.newton,
                R.drawable.tesla};
        contactNames = new String[] {" Carl Sagan"," Albert Einstein", " Isaac Newton"," Nikola Tesla"};
        contactList = (ListView) findViewById(R.id.listView2);
        contactListAdapter = new contactsListAdapter(this, contactNames, contactImages);

        contactList.setAdapter(contactListAdapter);


        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // On Click CheckBox will be checked
                        //contactCheckox.toggle();
                        break;

                    case 1:
                        // On Click CheckBox will be checked
                        //contactCheckox.toggle();
                        break;
                    case 2:
                        // On Click CheckBox will be checked break;
                        //contactCheckox.toggle();
                    case 3:
                        // On Click CheckBox will be checked
                        //contactCheckox.toggle();
                        break;
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_from_groups, menu);
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

    public void enterSendInvite(View view)
    {
        Intent enterSendInvites = new Intent(this, SendInvitesActivity.class);
        startActivity(enterSendInvites);
    }

}
