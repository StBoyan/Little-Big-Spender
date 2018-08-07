package com.boyanstoynov.littlebigspender.statistics;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.dao.CategoryDao;
import com.boyanstoynov.littlebigspender.db.dao.TransactionDao;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Transaction;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.realm.RealmResults;

/**
 * Controller for statistics activity.
 *
 * @author Boyan Stoynov
 */
public class StatisticsActivity extends BaseActivity {

    @BindView(R.id.toolbar_statistics) Toolbar toolbar;
    @BindView(R.id.tabLayout_statistics) TabLayout tabLayout;
    @BindView(R.id.viewPager_statistics) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_statistics);

        StatisticsFragmentPagerAdapter adapter = new StatisticsFragmentPagerAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_statistics;
    }

}
