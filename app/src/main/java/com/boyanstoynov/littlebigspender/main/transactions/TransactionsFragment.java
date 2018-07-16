package com.boyanstoynov.littlebigspender.main.transactions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.BaseFragment;

import butterknife.OnClick;

public class TransactionsFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO consider storing activity reference as class variable
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(R.string.all_transactions);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_transactions;
    }

    @OnClick(R.id.fab_transactions)
    public void startAddTransactionActivity() {
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        final Intent i = new Intent(parentActivity, AddTransactionActivity.class);
        startActivity(i);
    }
}
