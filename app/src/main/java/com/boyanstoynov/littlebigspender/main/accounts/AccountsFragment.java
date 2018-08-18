package com.boyanstoynov.littlebigspender.main.accounts;

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

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.BaseFragment;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.main.MainActivity;

import java.util.List;

import butterknife.BindView;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Accounts fragment which contains the accounts within the
 * MainActivity. Handles creating and managing the RecyclerView
 * and updating its information.
 *
 * @author Boyan Stoynov
 */
public class AccountsFragment extends BaseFragment {

    @BindView(R.id.recyclerView_accounts) RecyclerView recyclerView;

    private AccountsAdapter adapter;
    private RealmResults<Account> accountsRealmResults;

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
    public void onDestroyView() {
        super.onDestroyView();
        accountsRealmResults.removeAllChangeListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
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

    /**
     * Initialise the adapter and recycler view.
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
     * Load the account list for the first time and set a database
     * change listener.
     */
    private void loadAccountList() {
        accountsRealmResults = getRealmManager().createAccountDao().getAll();
        accountsRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Account>>() {
            @Override
            public void onChange(@NonNull RealmResults<Account> accounts) {
                populateRecyclerView(accounts);
            }
        });

        populateRecyclerView(accountsRealmResults);
    }

    /**
     * Populate the RecyclerView with list of accounts.
     * @param accountsList list of accounts
     */
    private void populateRecyclerView(List<Account> accountsList) {
        adapter.setData(accountsList);
    }
}

