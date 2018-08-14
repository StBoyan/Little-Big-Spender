package com.boyanstoynov.littlebigspender.recurring;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boyanstoynov.littlebigspender.BaseFragment;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.dao.RecurringDao;
import com.boyanstoynov.littlebigspender.db.model.Recurring;

import java.util.List;

import butterknife.BindView;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

import static com.boyanstoynov.littlebigspender.recurring.RecurringActivity.CATEGORY_TYPE_KEY;
import static com.boyanstoynov.littlebigspender.recurring.RecurringActivity.INCOME_TYPE_VALUE;

/**
 * Recurring transactions fragment which contains the selected
 * recurring transactions within the RecurringActivity. Handles
 * creating and managing RecyclerView and updating its information.
 *
 * @author Boyan Stoynov
 */
public class RecurringFragment extends BaseFragment {

    @BindView(R.id.recyclerview_recurring) RecyclerView recyclerView;

    private RecurringAdapter adapter;
    private RealmResults<Recurring> recurringRealmResults;
    private Bundle categoryType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Get bundle and store it so that it can be used
        upon configuration change as well */
        categoryType = getArguments();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        setCategoryType();
        initViews();
        loadRecurringList();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recurringRealmResults.removeAllChangeListeners();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recurring;
    }

    /**
     * Set the type of recurring transactions that this fragment will display.
     */
    protected void setCategoryType() {
        RecurringDao recurringDao = getRealmManager().createRecurringDao();

        if (categoryType.getString(CATEGORY_TYPE_KEY).equals(INCOME_TYPE_VALUE))
            recurringRealmResults = recurringDao.getAllIncomeRecurringTransactions();
        else
            recurringRealmResults = recurringDao.getAllExpenseRecurringTransactions();
    }

    /**
     * Initialise the adapter and recycler view.
     */
    private void initViews() {
        adapter = new RecurringAdapter((RecurringActivity)getActivity());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    /**
     * Loads the recurring transactions list for the first time and set
     * a database change listener.
     */
    private void loadRecurringList() {
        recurringRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Recurring>>() {
            @Override
            public void onChange(@NonNull RealmResults<Recurring> recurringList) {
                populateRecyclerView(recurringList);
            }
        });

        populateRecyclerView(recurringRealmResults);
    }

    /**
     * Populates the RecyclerView with list of recurring transactions.
     * @param recurringList list of recurring transactions
     */
    private void populateRecyclerView(List<Recurring> recurringList) {
        adapter.setData(recurringList);
    }
}
