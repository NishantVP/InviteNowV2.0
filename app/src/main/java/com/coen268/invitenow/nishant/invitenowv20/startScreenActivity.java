package com.coen268.invitenow.nishant.invitenowv20;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.parse.Parse;
import com.parse.ParseObject;


public class startScreenActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "nOjQbfKBEdY3A2rYAM5JmhPITjtO4A1DJeJq7iD1",
                "3LHhgD5smXqrZmkSVbjU4RWMsuDfrinANHjR3YU5");
        /*
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar1");
        testObject.saveInBackground();
        */


/*      Set Start screen Image Here. Random image is set at this moment.
        ImageView startScreenImage = (ImageView) findViewById(R.id.startScreenImage);
        startScreenImage.setImageResource(R.drawable.panda);
*/
    }

    public void enterApplication(View view)
    {
        Intent enterSendInvites = new Intent(this, SendInvitesActivity.class);
        startActivity(enterSendInvites);

/*      Ability to change the Start Screen Image on tap. Random image is set at this moment.
        ImageView startScreenImage = (ImageView) findViewById(R.id.startScreenImage);
        startScreenImage.setImageResource(R.drawable.elephant);
*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_screen, menu);
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
