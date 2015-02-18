package com.coen268.invitenow.nishant.invitenowv20;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class SelectFromGroupsActivity extends ActionBarActivity {

    private String[] groupNames;
    private int[] groupImages;
    private ListView groupList;
    private groupListAdapter groupListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_from_groups);

        groupImages = new int[] {R.drawable.elephant,R.drawable.elephant,R.drawable.elephant,
                R.drawable.elephant};
        groupNames = new String[] {" Group 1 "," Group 2", " Group 3"," Group 4"};
        groupList = (ListView) findViewById(R.id.listView3);
        groupListAdapter = new groupListAdapter(this, groupNames, groupImages);

        groupList.setAdapter(groupListAdapter);


        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // On Click CheckBox will be checked

                        break;

                    case 1:
                        // On Click CheckBox will be checked

                        break;
                    case 2:
                        // On Click CheckBox will be checked
                        break;

                    case 3:
                        // On Click CheckBox will be checked

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
