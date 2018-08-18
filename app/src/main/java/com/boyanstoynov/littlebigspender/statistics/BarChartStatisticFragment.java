package com.boyanstoynov.littlebigspender.statistics;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boyanstoynov.littlebigspender.BaseFragment;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Transaction;
import com.boyanstoynov.littlebigspender.util.SharedPrefsManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.realm.RealmResults;

/**
 * Bar chart fragment which gets the data for each fiat account's
 * income and expense and visualises it with a stacked bar chart.
 *
 * @author Boyan Stoynov
 */
public class BarChartStatisticFragment extends BaseFragment {

    @BindView(R.id.barChart_accountStatistic) BarChart chart;

    DecimalFormat fiatFormatter = new DecimalFormat("###,###,###,##0.00");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        final RealmResults<Account> accounts = getRealmManager().createAccountDao().getAll();
        List<BarEntry> dataEntries = new ArrayList<>();
        final List<String> labels = new ArrayList<>();

        // Loop through fiat accounts and calculate income and expense for each one
        int i = 0;
        for (Account account : accounts) {
            if (!account.isCrypto()) {
                labels.add(account.getName());
                RealmResults<Transaction> accountTransactions = getRealmManager().createTransactionDao().getByAccount(account);
                float accountIncome = 0.0f;
                float accountExpense = 0.0f;

                for (Transaction transaction : accountTransactions) {
                    if (transaction.getCategory().getType() == Category.Type.INCOME)
                        accountIncome += transaction.getAmount().floatValue();
                    else
                        accountExpense += transaction.getAmount().floatValue();
                }

                dataEntries.add(new BarEntry(i, new float[]{accountIncome, accountExpense}));
                i++;
            }
        }

        BarDataSet dataSet = new BarDataSet(dataEntries, "");
        dataSet.setColors(ContextCompat.getColor(getContext(), R.color.green),
                ContextCompat.getColor(getContext(), R.color.red));
        dataSet.setStackLabels(new String[]{getResources().getString(R.string.all_income), getResources().getString(R.string.all_expense)});

        BarData data = new BarData(dataSet);

        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int) value);
            }
        });
        chart.getXAxis().setLabelCount(labels.size());
        chart.setDrawGridBackground(false);

        chart.getAxisLeft().setAxisMinimum(0f);
        chart.getAxisRight().setAxisMinimum(0f);

        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return SharedPrefsManager.getCurrencySymbol() + " " + fiatFormatter.format(value);
            }
        });

        chart.setFitBars(true);
        chart.setData(data);

        chart.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return SharedPrefsManager.getCurrencySymbol() + " " + fiatFormatter.format(value);
            }
        });

        chart.getAxisRight().setEnabled(false);

        chart.invalidate();
        chart.animateY(1500);
        return view;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_bar_chart_statistic;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && chart != null)
            chart.animateY(1500);
    }
}
