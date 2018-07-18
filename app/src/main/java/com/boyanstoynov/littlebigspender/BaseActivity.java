package com.boyanstoynov.littlebigspender;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;

/**
 * Base class for Activities.
 *
 * @author Boyan Stoynov
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        // Binds ButterKnife to activity
        ButterKnife.bind(this);
    }

    /**
     * Gets the id of the layout that will fill the activity.
     * @return int layout id
     */
    protected abstract int getLayoutResource();
}
