package com.boyanstoynov.littlebigspender.statistics;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boyanstoynov.littlebigspender.BaseFragment;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.dao.TransactionDao;
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

/**
 * Bar Chart Fragment for Income and Expense statistics.
 *
 * @author Boyan Stoynov
 */
public class PieChartStatisticFragment extends BaseFragment {

    @BindView(R.id.pieChart_incomeExpenseStatistic) PieChart chart;
    Category.Type statisticType;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (statisticType == null)
            throw new IllegalStateException("Statistic Type not set. Need to call setStatisticType() first.");
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

        //TODO may need to reconsider architecture (i.e. no direct access to database)
        // TODO could make it extend a special fragment class that has access to database (for convenience)
        //TODO or somehow take it from the activity
        TransactionDao transactionDao = getRealmManager().createTransactionDao();
        final RealmResults<Category> categories;

        if (statisticType == Category.Type.INCOME)
            categories = getRealmManager().createCategoryDao().getAllIncomeCategories();
        else
            categories = getRealmManager().createCategoryDao().getAllExpenseCategories();

        List<PieEntry> dataEntries = new ArrayList<>();

        int i = 0;
        for (Category category : categories) {
            RealmResults<Transaction> categoryTransactions = transactionDao.getByCategory(category);
            float categorySum = 0.0f;

            for (Transaction transaction : categoryTransactions) {
                categorySum += transaction.getAmount().floatValue();
            }

            dataEntries.add(new PieEntry(categorySum, category.getName()));
            i++;
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

    protected void setStatisticType(Category.Type statisticType) {
        this.statisticType = statisticType;
    }

    // TODO could implement choosing colors here
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
