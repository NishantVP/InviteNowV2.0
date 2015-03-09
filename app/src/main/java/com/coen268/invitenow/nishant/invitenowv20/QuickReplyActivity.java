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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;


public class QuickReplyActivity extends ActionBarActivity {

    String message;
    String sender;
    String senderChannel;
    String usernamePhone,objectId,FirstName,LastName;
    String replyMessage = "NotUpdated";
    Button Yes,No,Later;
    TextView MessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_reply);

        MessageView = (TextView) findViewById(R.id.messageTextView);

        readFromDB();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String jsonData = extras.getString( "com.parse.Data" );
        System.out.println(jsonData);

         message= "Not Known";
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
            sender = message.substring(0,Math.min(message.length(),10));
            MessageView.setText(message);
            System.out.println("Sender: " +sender);
        }
        boolean isThisReply = sender.equals("Reply from");
        if(isThisReply == true )
        {
            Yes= (Button) findViewById(R.id.YesButton);
            No= (Button) findViewById(R.id.NoButton);
            Later= (Button) findViewById(R.id.LaterButton);

            Yes.setEnabled(false);
            No.setEnabled(false);
            Later.setEnabled(false);
            System.out.println("Should have been Disabled");


        }

        senderChannel = "ch"+sender;

    }


    public void saveMessageToSQLite(String message) {

        SQLiteDatabase db = new invitesDB(this).getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(invitesDB.COLUMN_MESSAGE, message);

        db.insert(invitesDB.DATABASE_TABLE, null, newValues);

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

    public void YesButtonClicked(View view)
    {
        replyMessage = "Reply from " + usernamePhone + " Yes";
        JSONObject data = new JSONObject();
        try {
            data.put("sender", "SampleData");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(data);

        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("channels", senderChannel);

        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query

        //ParsePush push = new ParsePush();
        //push.setChannel("ch4085655175");
        push.setMessage(replyMessage);

        //push.setData(data);
        push.sendInBackground();

    }



    public void NoButtonClicked(View view)
    {
        replyMessage = "Reply from " + usernamePhone + " No";
        JSONObject data = new JSONObject();
        try {
            data.put("sender", "SampleData");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(data);

        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("channels", senderChannel);

        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query

        //ParsePush push = new ParsePush();
        //push.setChannel("ch4085655175");
        push.setMessage(replyMessage);

        //push.setData(data);
        push.sendInBackground();

    }


    public void LaterButtonClicked(View view)
    {
        replyMessage = "Reply from " + usernamePhone + " After sometime";
        JSONObject data = new JSONObject();
        try {
            data.put("sender", "SampleData");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(data);

        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("channels", senderChannel);

        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query

        //ParsePush push = new ParsePush();
        //push.setChannel("ch4085655175");
        push.setMessage(replyMessage);

        //push.setData(data);
        push.sendInBackground();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quick_reply, menu);
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
