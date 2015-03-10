package com.coen268.invitenow.nishant.invitenowv20;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@SuppressWarnings("ALL")
public class SelectFromGroupsActivity extends ActionBarActivity {

    ListView listView;
    EditText editText;
    Cursor cursor;
    Context context;
    Long group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_from_groups);
        Intent intent = getIntent();

        context = this;
        listView = (ListView)findViewById(R.id.mylist);
        editText = (EditText)findViewById(R.id.textview);

        try {
            // Create an array of Strings, for List
            ArrayAdapter<Model> adapter = new InteractiveArrayAdapter(this, getModel());

            // Assign adapter to ListView
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    //Toast.makeText(getApplicationContext(),"Click ListItem Number " + position, Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            Log.d("*****exception",e.getMessage());
        }
    }

    private List<Model> getModel(){

        List<Model> list = new ArrayList<Model>();

        try{
            ContentResolver cr = getContentResolver();

            cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

            // changing this :: ContactsContract.CommonDataKinds.Phone.CONTENT_URI  to this :: ContactsContract.Groups.CONTENT_URI

            cursor.moveToFirst();
            if(cursor.moveToFirst()){
                while(cursor.moveToNext()) {

                     String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                     String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                     String s = name + "\n" + number;
                 //   String s = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE)); // this is the query to return the groups.
                  if(!list.contains(s)){

                    list.add(get(s));
                    s = null;
                  }
                }
            }
        }catch (Exception e){
            Log.d("********** error in Contact read: ",""+e.getMessage());
        }
        return list;
    }

    private Model get(String s) {
        return new Model(s);
    }

    public void onClick(View v){
        switch(v.getId()){

            case R.id.mybutton:
                String s="";
                for(int i=0; i<InteractiveArrayAdapter.list.size();i++){
                    if(InteractiveArrayAdapter.list.get(i).isSelected()){

                        s = s + i + " ";
                    }
                }
                String s1 = null;
                s1 = editText.getText().toString();

                // Check the edittext is empty or not
                if(s1.equals("")){
                    //Toast.makeText(SelectFromGroupsActivity.this, "Please Enter Any Text", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor grpCursor;
                String[] GROUP_PROJECTION = new String[]{ContactsContract.Groups._ID,ContactsContract.Groups.TITLE};
                grpCursor = this.managedQuery(ContactsContract.Groups.CONTENT_URI, GROUP_PROJECTION,ContactsContract.Groups.TITLE + "=?",new String[]{s1},ContactsContract.Groups.TITLE + " ASC");
                Log.d("**** Here Counts: ", " **" +grpCursor.getCount());

                if(grpCursor.getCount()>0){
                    //Toast.makeText(SelectFromGroupsActivity.this,"Group is already present",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    //Toast.makeText(SelectFromGroupsActivity.this,"Not available",Toast.LENGTH_SHORT).show();

                    try{
                        ContentValues groupValues;
                        ContentResolver cr = this.getContentResolver();
                        groupValues = new ContentValues();
                        groupValues.put(ContactsContract.Groups.TITLE,s1);
                        cr.insert(ContactsContract.Groups.CONTENT_URI,groupValues);
                        Log.d("######### Group Creation Finished: ", "##### Success");

                    }catch (Exception e){
                        Log.d("***** Exception : ", " " +e.getMessage());
                    }
                    //Toast.makeText(SelectFromGroupsActivity.this,"Created Successfully",Toast.LENGTH_SHORT).show();
                }

                grpCursor.close();
                grpCursor = null;


                Log.d(" ******* Contacts add to Groups ...","**** Fine");

                String groupID = null;
                Cursor  getGroupID_Cursor = null;
                getGroupID_Cursor = this.managedQuery(ContactsContract.Groups.CONTENT_URI, GROUP_PROJECTION,ContactsContract.Groups.TITLE + "=?", new String[]{s1},null);
                Log.d("****** Empty cursor size: ", "** "+getGroupID_Cursor.getCount());
                getGroupID_Cursor.moveToFirst();
                Log.d("**** Group ID is: ", "** "+groupID);

                getGroupID_Cursor.close();
                getGroupID_Cursor = null;


                for(int i=0; i<InteractiveArrayAdapter.list.size();i++){
                    if(InteractiveArrayAdapter.list.get(i).isSelected()){
                        cursor.moveToPosition(i);
                        String contactID = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

                        long contact = Long.parseLong(contactID);
                        group = Long.parseLong(groupID);

                        addToGroup(contact,group);

                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        Log.d("***** Contact Added : "," *: " +name);
                        //Toast.makeText(SelectFromGroupsActivity.this,name+ "added successfully", Toast.LENGTH_SHORT).show();

                    }
                }

                break;
        }
    }

    public Uri addToGroup(long personID,long groupID) {
        ContentValues values = new ContentValues();
        values.put(ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID,personID);
        values.put(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID, groupID);
        values.put(ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE,ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE);

        return this.context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI,values);
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
