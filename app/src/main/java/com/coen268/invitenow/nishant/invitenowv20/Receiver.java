package com.coen268.invitenow.nishant.invitenowv20;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nishant on 3/7/2015.
 */
public class Receiver extends ParsePushBroadcastReceiver {
    String message;
    private Context context;
    String sender;
    String replyMessage;
    String senderChannel;

    @Override
    public void onPushOpen(Context context, Intent intent) {
        Log.e("Push", "Clicked");
        Intent i = new Intent(context, QuickReplyActivity.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }


    protected void onPushDismiss(Context context,Intent intent)
    {
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
            //saveMessageToSQLite(message);
            sender = message.substring(0,Math.min(message.length(),10));
            senderChannel = "ch"+sender;

            Toast.makeText(context, "Message: " +message +" Not saved" , Toast.LENGTH_LONG).show();
            NegativeReply();
        }


    }

    public void NegativeReply()
    {
        replyMessage = "Reply from Someone you invited - Busy !";
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

    public void saveMessageToSQLite(String message) {

        //SQLiteDatabase db = new invitesDB(this).getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(invitesDB.COLUMN_MESSAGE, message);

        //db.insert(invitesDB.DATABASE_TABLE, null, newValues);

    }
}




