package com.boyanstoynov.littlebigspender.main.accounts;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.BaseFragment;
import com.boyanstoynov.littlebigspender.db.dao.AccountDao;
import com.boyanstoynov.littlebigspender.db.model.Account;

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
    private RealmResults<Account> accountsRealmResults;
    private AccountDao accountDao;

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
        adapter = new AccountsAdapter(this);

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
        accountDao = getRealmManager().createAccountDao();
        accountsRealmResults = accountDao.getAll();
        accountsRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Account>>() {
            @Override
            public void onChange(RealmResults<Account> accounts) {
                populateRecyclerView(accounts);
            }
        });

        populateRecyclerView(accountsRealmResults);
    }

    /**
     * Populate view with accounts and notify adapter of
     * data change.
     * @param accountsList List of Account objects
     */
    private void populateRecyclerView(List<Account> accountsList) {
        adapter.setData(accountsList);
    }

    public void onDeleteButtonClicked(final Account account) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.app_name);
        builder.setMessage(String.format("%s %s?", getResources().getString(R.string.all_warning_delete_message), account.getName()));
        builder.setIcon(R.drawable.ic_warning);
        builder.setPositiveButton(R.string.all_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                accountDao.delete(account);
                Toast.makeText(getContext(), R.string.accounts_delete_toast, Toast.LENGTH_SHORT).show();
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
}

