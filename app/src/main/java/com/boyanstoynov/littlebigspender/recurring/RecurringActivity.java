package com.boyanstoynov.littlebigspender.recurring;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Category;

import butterknife.BindView;

/**
 * Controller for recurring transactions activity.
 *
 * @author Boyan Stoynov
 */
public class RecurringActivity extends BaseActivity {

    @BindView(R.id.toolbar_recurring) Toolbar toolbar;
    @BindView(R.id.tablayout_recurring) TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_recurring);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                RecurringFragment recurringFragment = new RecurringFragment();
                switch (tab.getPosition()) {
                    case 0:
                        recurringFragment.setCategoryType(Category.Type.INCOME);
                        break;
                    case 1:
                        recurringFragment.setCategoryType(Category.Type.EXPENSE);
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_recurring, recurringFragment);
                transaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.getTabAt(1).select();
        tabLayout.getTabAt(0).select();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_recurring;
    }
}
