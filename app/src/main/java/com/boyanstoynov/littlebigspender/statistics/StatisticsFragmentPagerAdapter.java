package com.boyanstoynov.littlebigspender.statistics;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Category;

/**
 * Statistic Fragment Pager Adapter.
 *
 * @author Boyan Stoynov
 */
public class StatisticsFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int NUMBER_OF_TABS = 3;
    private final Context context;

    StatisticsFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                //TODO may want to rename class for clarity (e.g. pieChart)
                IncomeExpenseStatisticFragment incomeStatistic = new IncomeExpenseStatisticFragment();
                incomeStatistic.setStatisticType(Category.Type.INCOME);
                return incomeStatistic;
            case 1:
                IncomeExpenseStatisticFragment expenseStatistic = new IncomeExpenseStatisticFragment();
                expenseStatistic.setStatisticType(Category.Type.EXPENSE);
                return expenseStatistic;
            case 2:
                return new AccountStatisticFragment();
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
        //TODO may want to extract numbers to finals (remove magic numbers)
        switch (position) {
            case 0:
                return context.getString(R.string.statistics_income_breakdown);
            case 1:
                return context.getString(R.string.statistics_expense_breakdown);
            case 2:
                return context.getString(R.string.statistics_accounts_cashFlow);
            default:
                return null;
        }
    }
}
