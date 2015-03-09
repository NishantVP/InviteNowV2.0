package com.coen268.invitenow.nishant.invitenowv20;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class NearbyFriendsActivity extends ActionBarActivity {
    ListView FriendListView;
    ArrayList<String> friendList = new ArrayList<>();
    String[] phoneNumbers = new String[20];
    String[] FirstNames = new String[20];
    String[] LastNames = new String[20];
    String message = "This message is sent automatically by Nishant from InviteNow App ";
    public View row;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nearby_friends);
        FriendListView=(ListView)findViewById(R.id.nearbyFriendsList);

        readFromDB();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.friend_list_item,friendList);
        FriendListView.setAdapter(adapter);

        FriendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int p,
                                    long arg3) {

                if (row != null) {
                    row.setBackgroundResource(R.color.accent_material_dark);
                }
                row = arg1;
                arg1.setBackgroundResource(R.color.accent_material_light);

                saveRecipientToSQLite(phoneNumbers[p],FirstNames[p],LastNames[p]);

                /*
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumbers[p],null,message,null,null);
                Toast.makeText(NearbyFriendsActivity.this, "message sent to: " + phoneNumbers[p],
                        Toast.LENGTH_SHORT).show();

                Intent viewNote = new Intent(NearbyFriendsActivity.this, SendInvitesActivity.class);
                viewNote.putExtra("NoteID", p);
                startActivity(viewNote);
                */


            }
        });
     /*
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String jsonData = extras.getString( "com.parse.Data" );
        System.out.println(jsonData);

        String message = "Not Known";
        try {

            JSONObject obj = new JSONObject(jsonData);

            Log.d("My App", obj.toString());
            message = obj.getString("alert");

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + jsonData + "\"");
        }

        System.out.println(message);
     */
    }

    public void saveRecipientToSQLite(String rusername,
                                      String rfirstname,String rlastname ) {

        SQLiteDatabase db = new recipientsDB(this).getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(recipientsDB.COLUMN_RECIPIENT_USERNAME, rusername);
        newValues.put(recipientsDB.COLUMN_RECIPIENT_FIRSTNAME, rfirstname);
        newValues.put(recipientsDB.COLUMN_RECIPIENT_LASTNAME, rlastname);

        db.insert(recipientsDB.DATABASE_TABLE, null, newValues);
        System.out.println("Saved in Recipients Table" + rusername);

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

            String username = cursor.getString(cursor.getColumnIndex(friendLocationDB.COLUMN_FRIEND_USERNAME));
            String firstname = cursor.getString(cursor.getColumnIndex(friendLocationDB.COLUMN_FRIEND_FIRSTNAME));
            String lastname = cursor.getString(cursor.getColumnIndex(friendLocationDB.COLUMN_FRIEND_LASTNAME));
            /* For Debug */
            //String name = cursor.getString(cursor.getColumnIndex(NotesDB.COLUMN_FRIEND_FIRSTNAME));
            /* For Debug */
            if(!friendList.contains(username)) {
                phoneNumbers[i]=username;
                FirstNames[i]=firstname;
                LastNames[i]=lastname;
                friendList.add(username);
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
