package com.boyanstoynov.littlebigspender.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Controller for settings activity.
 *
 * @author Boyan Stoynov
 */
public class SettingsActivity extends BaseActivity {

    @BindView(R.id.toolbar_settings) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_settings);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_settings;
    }

    //TODO remove temporary debugging method.
    @OnClick(R.id.button_settings_reset)
    public void resetPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor e = prefs.edit();

        e.remove("firstStart");
        e.apply();
    }
}
