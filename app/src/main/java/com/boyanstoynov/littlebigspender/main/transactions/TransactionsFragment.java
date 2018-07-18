package com.boyanstoynov.littlebigspender.main.transactions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.BaseFragment;

import butterknife.OnClick;

/**
 * Controller for Transactions fragment of main screen.
 *
 * @author Boyan Stoynov
 */
public class TransactionsFragment extends BaseFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO consider storing activity reference as class variable
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(R.string.all_transactions);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_transactions;
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
