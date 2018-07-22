package com.boyanstoynov.littlebigspender.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.main.accounts.AccountsFragment;
import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.main.overview.OverviewFragment;
import com.boyanstoynov.littlebigspender.main.transactions.TransactionsFragment;

import com.boyanstoynov.littlebigspender.about.AboutActivity;
import butterknife.BindView;
import com.boyanstoynov.littlebigspender.categories.CategoriesActivity;
import com.boyanstoynov.littlebigspender.intro.IntroActivity;
import com.boyanstoynov.littlebigspender.recurring.RecurringActivity;
import com.boyanstoynov.littlebigspender.settings.SettingsActivity;
import com.boyanstoynov.littlebigspender.statistics.StatisticsActivity;

/**
 * Controller for home screen activity.
 *
 * @author Boyan Stoynov
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.drawer) DrawerLayout drawer;
    @BindView(R.id.toolbar_main) Toolbar toolbar;
    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.drawer_navigation) NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startIntroOnFirstLaunch();

        super.onCreate(savedInstanceState);
        //TODO consider making toolbar part of a superclass to use in other activities
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer_menu);


        //TODO consider putting listener methods separately to reduce size of onCreate
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.item_add:
                                selectedFragment = new OverviewFragment();
                                break;
                            case R.id.item_transactions:
                                selectedFragment = new TransactionsFragment();
                                break;
                            case R.id.item_accounts:
                                selectedFragment = new AccountsFragment();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_main, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                }
        );

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(true);
                        drawer.closeDrawers();
                        Intent i = null;

                        switch (item.getItemId()) {
                            case R.id.item_settings:
                                i = new Intent(MainActivity.this, SettingsActivity.class);
                                break;
                            case R.id.item_categories:
                                i = new Intent(MainActivity.this, CategoriesActivity.class);
                                break;
                            case R.id.item_recurring:
                                i = new Intent(MainActivity.this, RecurringActivity.class);
                                break;
                            case R.id.item_statistics:
                                i = new Intent(MainActivity.this, StatisticsActivity.class);
                                break;
                            case R.id.item_about:
                                i = new Intent(MainActivity.this, AboutActivity.class);
                                break;
                        }

                        startActivity(i);
                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(Gravity.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    /**
     * Launch Intro activity upon first launch.
     */
    public void startIntroOnFirstLaunch() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                if (isFirstStart) {

                    final Intent i = new Intent(MainActivity.this, IntroActivity.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(i);
                        }
                    });

                    SharedPreferences.Editor e = getPrefs.edit();

                    e.putBoolean("firstStart", false);

                    e.apply();
                }
            }
        });

        t.start();
    }
}
