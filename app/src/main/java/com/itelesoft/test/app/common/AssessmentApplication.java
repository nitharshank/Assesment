package com.itelesoft.test.app.common;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.itelesoft.test.app.BuildConfig;
import com.itelesoft.test.app.database.ProjectDatabase;

public class AssessmentApplication extends Application {

    // Constants
    private static final String TAG = AssessmentApplication.class.getSimpleName();

    // Objects
    private static ProjectDatabase projectDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        projectDatabase = ProjectDatabase.getDatabase(getApplicationContext());

        // Stetho for local DB view and network monitor
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);//chrome://inspect/#devices
        }
    }

    public static ProjectDatabase getRoomSession() {
        return projectDatabase;
    }

}
