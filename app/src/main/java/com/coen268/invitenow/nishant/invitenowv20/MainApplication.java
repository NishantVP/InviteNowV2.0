package com.coen268.invitenow.nishant.invitenowv20;

import android.app.Application;
import android.content.Context;
import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by Nishant on 3/5/2015.
 */
public class MainApplication extends Application {
    private static MainApplication instance = new MainApplication();

    public MainApplication() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "nOjQbfKBEdY3A2rYAM5JmhPITjtO4A1DJeJq7iD1",
                "3LHhgD5smXqrZmkSVbjU4RWMsuDfrinANHjR3YU5");

        ParseInstallation.getCurrentInstallation().saveInBackground();
        //Toast.makeText(getApplicationContext(), "in app class",
          //      Toast.LENGTH_SHORT).show();
        //System.out.println("Application class was run");

    }
}
