package com.boyanstoynov.littlebigspender;

import android.app.Application;

import io.realm.Realm;

/**
 * Application class which initialises Realm database.
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
