package com.coen268.invitenow.nishant.invitenowv20;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class GroupsActivity extends ActionBarActivity {

    ListView myList;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        myList = (ListView)findViewById(R.id.listGrp);

        ArrayAdapter<Model> adapter = new InteractiveArrayAdapter(this, getModel());

        // Assign adapter to ListView
        myList.setAdapter(adapter);


        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(), "Click ListItem Number " + position, Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<Model> getModel(){

        List<Model> list = new ArrayList<Model>();

        ContentResolver cr = getContentResolver();

        cursor = cr.query(ContactsContract.Groups.CONTENT_URI,null,null,null,ContactsContract.Groups.TITLE + " ASC");

        cursor.moveToFirst();
        if(cursor.moveToFirst()){
            while(cursor.moveToNext()) {

                String s = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE)); // this is the query to return the groups.
                if (!list.contains(s)) {
                    list.add(get(s));
                    s = null;
                }
            }
        }
        return list;
    }

    private Model get(String s) {
        return new Model(s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_groups, menu);
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
