package com.boyanstoynov.littlebigspender.statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Category;

import butterknife.BindView;

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

        StatisticsFragmentPagerAdapter adapter = new StatisticsFragmentPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_statistics;
    }

    /**
     * Inner class that implements the FragmentPagerAdapter.
     */
    public class StatisticsFragmentPagerAdapter extends FragmentPagerAdapter {

        private final int NUMBER_OF_TABS = 3;

        private final int INCOME_BREAKDOWN_POSITION = 0;
        private final int EXPENSE_BREAKDOWN_POSITION = 1;
        private final int ACCOUNT_CASHFLOW_POSITION = 2;

        StatisticsFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    PieChartStatisticFragment incomeStatistic = new PieChartStatisticFragment();
                    incomeStatistic.setStatisticType(Category.Type.INCOME);
                    return incomeStatistic;
                case 1:
                    PieChartStatisticFragment expenseStatistic = new PieChartStatisticFragment();
                    expenseStatistic.setStatisticType(Category.Type.EXPENSE);
                    return expenseStatistic;
                case 2:
                    return new BarChartStatisticFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUMBER_OF_TABS;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case INCOME_BREAKDOWN_POSITION:
                    return StatisticsActivity.this.getString(R.string.statistics_income_breakdown);
                case EXPENSE_BREAKDOWN_POSITION:
                    return StatisticsActivity.this.getString(R.string.statistics_expense_breakdown);
                case ACCOUNT_CASHFLOW_POSITION:
                    return StatisticsActivity.this.getString(R.string.statistics_accounts_cashFlow);
                default:
                    return null;
            }
        }
    }
}
