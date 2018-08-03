package com.boyanstoynov.littlebigspender.main.accounts;

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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.BaseFragment;
import com.boyanstoynov.littlebigspender.db.dao.AccountDao;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.main.MainActivity;

import java.util.List;

import butterknife.BindView;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Controller for Accounts fragment of main screen.
 *
 * @author Boyan Stoynov
 */
public class AccountsFragment extends BaseFragment {

    @BindView(R.id.recyclerview_accounts) RecyclerView recyclerView;

    private AccountsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(R.string.all_accounts);

        View view = super.onCreateView(inflater, container, savedInstanceState);

        initViews();
        loadAccountList();

        return view;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_accounts;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.addbutton_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //TODO consider tidying up method. May need to add more menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add:
                AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
                final Intent i = new Intent(parentActivity, AddAccountActivity.class);
                startActivity(i);
                break;
        }

        return true;
    }

    /**
     * Initialise RecyclerView and its adapter.
     */
    private void initViews() {
        adapter = new AccountsAdapter((MainActivity)getActivity());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    /**
     * Load Account objects into view.
     */
    private void loadAccountList() {
        AccountDao accountDao = getRealmManager().createAccountDao();
        RealmResults<Account> accountsRealmResults = accountDao.getAll();
        accountsRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Account>>() {
            @Override
            public void onChange(@NonNull RealmResults<Account> accounts) {
                populateRecyclerView(accounts);
            }
        });

        populateRecyclerView(accountsRealmResults);
    }

    /** TODO change this comment here and in other fragments
     * Populate view with accounts and notify adapter of
     * data change.
     * @param accountsList List of Account objects
     */
    private void populateRecyclerView(List<Account> accountsList) {
        adapter.setData(accountsList);
    }
}

