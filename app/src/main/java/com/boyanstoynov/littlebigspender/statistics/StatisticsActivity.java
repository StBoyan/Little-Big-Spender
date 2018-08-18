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

import butterknife.BindView;

/**
 * Controller for statistics activity. Responsible for controlling the
 * ViewPager and setting its adapter.
 *
 * @author Boyan Stoynov
 */
public class StatisticsActivity extends BaseActivity {

    @BindView(R.id.toolbar_statistics) Toolbar toolbar;
    @BindView(R.id.tabLayout_statistics) TabLayout tabLayout;
    @BindView(R.id.viewPager_statistics) ViewPager viewPager;

    // Key and values used in bundles in this package
    protected static final String STATISTIC_TYPE_KEY = "statisticType";
    protected static final String STATISTIC_TYPE_INCOME = "typeIncome";
    protected static final String STATISTIC_TYPE_EXPENSE = "typeExpense";
    protected static final String STATISTIC_TYPE_CRYPTO = "typeCrypto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_statistics);

        StatisticsFragmentPagerAdapter adapter = new StatisticsFragmentPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_statistics;
    }

    /**
     * Inner class that implements the FragmentPagerAdapter. Adds the
     * fragments to the viewpager with the applicable arguments and
     * adds them to the view when needed.
     */
    public class StatisticsFragmentPagerAdapter extends FragmentPagerAdapter {

        private final int NUMBER_OF_TABS = 4;
        private final int INCOME_BREAKDOWN_POSITION = 0;
        private final int EXPENSE_BREAKDOWN_POSITION = 1;
        private final int ACCOUNT_CASHFLOW_POSITION = 2;
        private final int CRYPTO_BREAKDOWN_POSITION = 3;

        StatisticsFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            switch (position) {
                case INCOME_BREAKDOWN_POSITION:
                    PieChartStatisticFragment incomeStatistic = new PieChartStatisticFragment();
                    args.putString(STATISTIC_TYPE_KEY, STATISTIC_TYPE_INCOME);
                    incomeStatistic.setArguments(args);
                    return incomeStatistic;
                case EXPENSE_BREAKDOWN_POSITION:
                    PieChartStatisticFragment expenseStatistic = new PieChartStatisticFragment();
                     args.putString(STATISTIC_TYPE_KEY, STATISTIC_TYPE_EXPENSE);
                    expenseStatistic.setArguments(args);
                    return expenseStatistic;
                case ACCOUNT_CASHFLOW_POSITION:
                    return new BarChartStatisticFragment();
                case CRYPTO_BREAKDOWN_POSITION:
                    PieChartStatisticFragment cryptoStatistic = new PieChartStatisticFragment();
                    args.putString(STATISTIC_TYPE_KEY, STATISTIC_TYPE_CRYPTO);
                    cryptoStatistic.setArguments(args);
                    return cryptoStatistic;
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
                case CRYPTO_BREAKDOWN_POSITION:
                    return StatisticsActivity.this.getString(R.string.statistics_crypto_breakdown);
                default:
                    return null;
            }
        }
    }
}
