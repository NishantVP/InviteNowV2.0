package com.coen268.invitenow.nishant.invitenowv20;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;


public class NotificationActivity extends ActionBarActivity {

    ListView FriendListView;
    ArrayList<String> friendList = new ArrayList<>();
    String[] phoneNumbers = new String[20];
    String message = "This message is sent automatically by Nishant from InviteNow App ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);
        FriendListView=(ListView)findViewById(R.id.nearbyFriendsList);

        readFromDB();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.friend_list_item,friendList);
        FriendListView.setAdapter(adapter);

        FriendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int p,
                                    long arg3) {

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumbers[p],null,message,null,null);
                Toast.makeText(NotificationActivity.this, "message sent to: " + phoneNumbers[p],
                        Toast.LENGTH_SHORT).show();

                Intent viewNote = new Intent(NotificationActivity.this, SendInvitesActivity.class);
                viewNote.putExtra("NoteID", p);
                startActivity(viewNote);
            }
        });

    }

    @Override
    protected void onResume() {

        FriendListView=(ListView)findViewById(R.id.nearbyFriendsList);
        readFromDB();

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.friend_list_item, friendList);
        FriendListView.setAdapter(adapter);
        super.onResume();
    }

    public void readFromDB() {
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

            String name = cursor.getString(cursor.getColumnIndex(friendLocationDB.COLUMN_FRIEND_USERNAME));

            /* For Debug */
            //String name = cursor.getString(cursor.getColumnIndex(NotesDB.COLUMN_FRIEND_FIRSTNAME));
            /* For Debug */
            if(!friendList.contains(name)) {
                phoneNumbers[i]=name;
                friendList.add(name);
                i++;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification, menu);
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
