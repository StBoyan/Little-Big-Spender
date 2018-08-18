package com.boyanstoynov.littlebigspender.statistics;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boyanstoynov.littlebigspender.BaseFragment;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.dao.TransactionDao;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Transaction;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import io.realm.RealmResults;

import static com.boyanstoynov.littlebigspender.statistics.StatisticsActivity.STATISTIC_TYPE_CRYPTO;
import static com.boyanstoynov.littlebigspender.statistics.StatisticsActivity.STATISTIC_TYPE_EXPENSE;
import static com.boyanstoynov.littlebigspender.statistics.StatisticsActivity.STATISTIC_TYPE_INCOME;
import static com.boyanstoynov.littlebigspender.statistics.StatisticsActivity.STATISTIC_TYPE_KEY;

/**
 * Pie chart fragment which gets the data according to the argument
 * passed to it and visualises it in a Pie Chart formatted with
 * percentages.
 *
 * @author Boyan Stoynov
 */
public class PieChartStatisticFragment extends BaseFragment {

    @BindView(R.id.pieChart_incomeExpenseStatistic) PieChart chart;

    private TransactionDao transactionDao;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        chart.setUsePercentValues(true);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.setDrawHoleEnabled(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        List<PieEntry> dataEntries = null;

        // Depending on argument populates the pie chart with the relevant data
        switch (getArguments().getString(STATISTIC_TYPE_KEY)) {
            case STATISTIC_TYPE_INCOME:
                transactionDao = getRealmManager().createTransactionDao();
                List<Category> incomeCategories = getRealmManager().createCategoryDao().getAllIncomeCategories();
                dataEntries = getCategoryDataEntries(incomeCategories);
                break;
            case STATISTIC_TYPE_EXPENSE:
                transactionDao = getRealmManager().createTransactionDao();
                List<Category> expenseCategories = getRealmManager().createCategoryDao().getAllExpenseCategories();
                dataEntries = getCategoryDataEntries(expenseCategories);
                break;
            case STATISTIC_TYPE_CRYPTO:
                List<Account> cryptoAccounts = getRealmManager().createAccountDao().getAllCrypto();
                dataEntries = getAccountDataEntries(cryptoAccounts);
                break;
        }

        PieDataSet dataSet = new PieDataSet(dataEntries, "");
        dataSet.setColors(getColors());

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());

        chart.setData(data);
        chart.invalidate();
        chart.animateY(1500);

        return view;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_pie_chart_statistic;
    }

    /**
     * Gets PieChart data for the list of categories.
     * @param categories List of categories
     * @return List<PieEntry> pie chart data
     */
    private List<PieEntry> getCategoryDataEntries(List<Category> categories) {
        List<PieEntry> categoryEntries = new ArrayList<>();

        for (Category category : categories) {
            RealmResults<Transaction> categoryTransactions = transactionDao.getByCategory(category);
            float categorySum = 0.0f;

            for (Transaction transaction : categoryTransactions) {
                if (transaction.getAccount().isCrypto()) {
                    float fiatValue = transaction.getAccount().getFiatValue().floatValue();
                    if (fiatValue > 0.0f)
                        categorySum += transaction.getAmount().floatValue() * fiatValue;
                }
                else
                    categorySum += transaction.getAmount().floatValue();
            }

            categoryEntries.add(new PieEntry(categorySum, category.getName()));
        }

        return categoryEntries;
    }

    /**
     * Gets PieChart data for the list of accounts.
     * @param accounts List of accounts
     * @return LIst<PieEntry> pie chart data
     */
    private List<PieEntry> getAccountDataEntries(List<Account> accounts) {
        List<PieEntry> accountEntries = new ArrayList<>();

        for (Account account : accounts) {
            float accountBalance = account.getBalance().floatValue() *
                    account.getFiatValue().floatValue();
            if (accountBalance != 0.0f)
                accountEntries.add(new PieEntry(accountBalance, account.getName()));

        }
        return accountEntries;
    }

    /**
     * Gets all colors provided with the MPAndroidChart database
     * and add the to a List in a shuffled order.
     * @return List of colors
     */
    private List<Integer> getColors() {
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        Collections.shuffle(colors);
        return colors;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && chart != null)
            chart.animateY(1500);
    }
}
