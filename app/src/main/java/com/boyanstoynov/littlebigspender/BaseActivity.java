package com.boyanstoynov.littlebigspender;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.boyanstoynov.littlebigspender.db.RealmManager;

import butterknife.ButterKnife;

/**
 * Base class for Activities.
 *
 * @author Boyan Stoynov
 */
public abstract class BaseActivity extends AppCompatActivity {

    private RealmManager realmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        // Bind ButterKnife to activity
        ButterKnife.bind(this);
        realmManager = new RealmManager();
        realmManager.open();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmManager.close();
    }

    /**
     * Gets the instance of RealmManager for the activity.
     * @return RealmManager RealmManager instance
     */
    protected RealmManager getRealmManager() {
        return realmManager;
    }

    /**
     * Gets the id of the layout that will fill the activity.
     * @return int layout id
     */
    protected abstract int getLayoutResource();


}
