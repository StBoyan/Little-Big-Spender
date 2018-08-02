package com.boyanstoynov.littlebigspender.recurring;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boyanstoynov.littlebigspender.BaseFragment;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.RealmManager;
import com.boyanstoynov.littlebigspender.db.dao.RecurringDao;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Recurring;

import java.util.List;

import butterknife.BindView;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Controller for Recurring fragment.
 *
 * @author Boyan Stoynov
 */
public class RecurringFragment extends BaseFragment {

    @BindView(R.id.recyclerview_recurring) RecyclerView recyclerView;

    private RecurringAdapter adapter;
    private RealmResults<Recurring> recurringRealmResults;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        initViews();
        loadRecurringList();

        return view;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recurring;
    }

    protected void setCategoryType(Category.Type type) {
        //TODO remove temp solution. See CategoriesFragment for solution
        RealmManager rm = new RealmManager();
        rm.open();
        RecurringDao recurringDao = rm.createRecurringDao();

        if (type == Category.Type.INCOME)
            recurringRealmResults = recurringDao.getAllIncomeRecurringTransactions();
        else
            recurringRealmResults = recurringDao.getAllExpenseRecurringTransactions();
    }

    private void initViews() {
        adapter = new RecurringAdapter((RecurringActivity)getActivity());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void loadRecurringList() {
        if (recurringRealmResults == null) {
            throw new IllegalStateException("Categories type unspecified. Call setCategoryType() first.");
        }

        recurringRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Recurring>>() {
            @Override
            public void onChange(@NonNull RealmResults<Recurring> recurringList) {
                populateRecyclerView(recurringList);
            }
        });

        populateRecyclerView(recurringRealmResults);
    }

    private void populateRecyclerView(List<Recurring> recurringList) {
        adapter.setData(recurringList);
    }
}
