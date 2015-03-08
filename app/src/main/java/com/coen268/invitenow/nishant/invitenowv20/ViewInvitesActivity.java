package com.coen268.invitenow.nishant.invitenowv20;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;


public class ViewInvitesActivity extends ActionBarActivity {


    ListView MessageListView;
    ArrayList<String> messageList = new ArrayList<>();
    String[] Messages = new String[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invites);

        MessageListView=(ListView)findViewById(R.id.messageListView);
        readFromMessageDB();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.message_list_item,messageList);
        MessageListView.setAdapter(adapter);
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

        if(message!="Not Known") {
            System.out.println(message);
            saveMessageToSQLite(message);
        }
        */
    }


    public void saveMessageToSQLite(String message) {

        SQLiteDatabase db = new invitesDB(this).getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(invitesDB.COLUMN_MESSAGE, message);

        db.insert(invitesDB.DATABASE_TABLE, null, newValues);

    }

    public void readFromMessageDB() {
        SQLiteDatabase db = new invitesDB(this).getWritableDatabase();
        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        String[] resultColumns = {invitesDB.COLUMN_ID, invitesDB.COLUMN_MESSAGE};

        Cursor cursor = db.query(invitesDB.DATABASE_TABLE, resultColumns, where, whereArgs,
                groupBy, having, order);

        int i=0;
        while (cursor.moveToNext()) {
            int idInDB = cursor.getInt(cursor.getColumnIndex(invitesDB.COLUMN_ID));

            String message = cursor.getString(cursor.getColumnIndex(invitesDB.COLUMN_MESSAGE));

            /* For Debug */
            //String name = cursor.getString(cursor.getColumnIndex(NotesDB.COLUMN_FRIEND_FIRSTNAME));
            /* For Debug */
            if(!messageList.contains(message)) {
                Messages[i]=message;
                messageList.add(message);
                i++;
            }
        }
    }

    @Override
    protected void onResume() {

        MessageListView=(ListView)findViewById(R.id.messageListView);
        readFromMessageDB();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.message_list_item,messageList);
        MessageListView.setAdapter(adapter);

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_invites, menu);
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
