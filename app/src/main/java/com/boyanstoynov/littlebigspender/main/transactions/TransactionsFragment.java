package com.boyanstoynov.littlebigspender.main.transactions;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.boyanstoynov.littlebigspender.BaseFragment;
import com.boyanstoynov.littlebigspender.BaseRecyclerAdapter;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.dao.TransactionDao;
import com.boyanstoynov.littlebigspender.db.model.Transaction;

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
public class TransactionsFragment extends BaseFragment implements BaseRecyclerAdapter.RecyclerViewListener<Transaction>{

    @BindView(R.id.recyclerview_transactions) RecyclerView recyclerView;

    private TransactionsAdapter adapter;
    private TransactionDao transactionDao;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

    public void initViews() {
        adapter = new TransactionsAdapter(this);

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

    private void populateRecyclerView(List<Transaction> transactionList) {
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

    @Override
    public void onDeleteButtonClicked(final Transaction transaction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.transaction_warning_delete_message);
        builder.setIcon(R.drawable.ic_warning);
        builder.setPositiveButton(R.string.all_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                transactionDao.delete(transaction);
                Toast.makeText(getContext(), R.string.transaction_delete_toast, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.all_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onEditButtonClicked(Transaction item) {

    }
}

