package com.boyanstoynov.littlebigspender;

import android.app.Application;

import io.realm.Realm;

/**
 * Class to maintain application-wide state.
 *
 * @author Boyan Stoynov
 */
public class LittleBigSpender extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Initialise Realm database on launch with default config
        Realm.init(this);
    }
}
