package com.boyanstoynov.littlebigspender.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.boyanstoynov.littlebigspender.R;

public class AccountsFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(R.string.all_accounts);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_accounts;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.accounts_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //TODO consider tidying up method. May need to add more menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_addaccount:
                AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
                final Intent i = new Intent(parentActivity, AddAccountActivity.class);
                startActivity(i);
                break;
        }

        return true;
    }


}

