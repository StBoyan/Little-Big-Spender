package com.boyanstoynov.littlebigspender.main.transactions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boyanstoynov.littlebigspender.BaseFragment;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.dao.TransactionDao;
import com.boyanstoynov.littlebigspender.db.model.Transaction;
import com.boyanstoynov.littlebigspender.main.MainActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Controller for Transactions fragment of main screen.
 *
 * @author Boyan Stoynov
 */
public class TransactionsFragment extends BaseFragment {

    @BindView(R.id.recyclerview_transactions) RecyclerView recyclerView;

    private TransactionsAdapter adapter;
    private TransactionDao transactionDao;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        //TODO consider storing activity reference as class variable
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(R.string.all_transactions);

        View view = super.onCreateView(inflater, container, savedInstanceState);

        //TODO consider generalising these 2 methods and populateRecyclerView
        initViews();
        loadTransactionList();

        return view;
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

    public void initViews() {
        adapter = new TransactionsAdapter((MainActivity)getActivity());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void loadTransactionList() {
        transactionDao = getRealmManager().createTransactionDao();
        RealmResults<Transaction> transactionsRealmResults = transactionDao.getAll();
        transactionsRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Transaction>>() {
            @Override
            public void onChange(@NonNull RealmResults<Transaction> transactions) {
                populateRecyclerView(transactions);
            }
        });

        populateRecyclerView(transactionsRealmResults);
    }

    //TODO made public in order to make filter work. Need to refactor class so that it is controllable by activity and doesn't know anything about DAOs. Can also do the same for other recyclerviewfragments
    public void populateRecyclerView(List<Transaction> transactionList) {
        Log.d("adapter list size is", String.valueOf(transactionList.size()));
        adapter.setData(transactionList);
    }

    /**
     * Start the Add transaction activity when the FAB is clicked.
     */
    @OnClick(R.id.fab_transactions)
    public void startAddTransactionActivity() {
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        final Intent i = new Intent(parentActivity, AddTransactionActivity.class);
        startActivity(i);
    }
}

