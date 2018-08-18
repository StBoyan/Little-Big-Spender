package com.boyanstoynov.littlebigspender.main.transactions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boyanstoynov.littlebigspender.BaseFragment;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Transaction;
import com.boyanstoynov.littlebigspender.main.MainActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Transactions fragment which contains the transactions within the
 * MainActivity. Handles creating and managing the RecyclerView and
 * updating its information.
 *
 * @author Boyan Stoynov
 */
public class TransactionsFragment extends BaseFragment {

    @BindView(R.id.recyclerview_transactions) RecyclerView recyclerView;

    private TransactionsAdapter adapter;
    private RealmResults<Transaction> transactionsRealmResults;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(R.string.all_transactions);

        View view = super.onCreateView(inflater, container, savedInstanceState);

        initViews();
        loadTransactionList();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        transactionsRealmResults.removeAllChangeListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_transactions;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filterbutton_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Initialise the adapter and recycler view.
     */
    public void initViews() {
        adapter = new TransactionsAdapter((MainActivity)getActivity());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    /**
     * Load the account list for the first time and set a database
     * change listener.
     */
    public void loadTransactionList() {
        transactionsRealmResults = getRealmManager().createTransactionDao().getAll();
        transactionsRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Transaction>>() {
            @Override
            public void onChange(@NonNull RealmResults<Transaction> transactions) {
                populateRecyclerView(transactions);
            }
        });

        populateRecyclerView(transactionsRealmResults);
    }

    /**
     * Populate the RecyclerView with list of accounts.
     * @param transactionList list of accounts
     */
    public void populateRecyclerView(List<Transaction> transactionList) {
        adapter.setData(transactionList);
    }

    /**
     * Start the Add transaction activity when the FAB is clicked.
     */
    @OnClick(R.id.fab_transactions)
    public void startAddTransactionActivity() {
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        final Intent intent = new Intent(parentActivity, AddTransactionActivity.class);
        startActivity(intent);
    }
}

