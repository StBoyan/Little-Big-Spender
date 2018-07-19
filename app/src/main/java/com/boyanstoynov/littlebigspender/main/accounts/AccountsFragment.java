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

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.BaseFragment;
import com.boyanstoynov.littlebigspender.db.RealmManager;
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

    /**
     * @inheritDoc
     */
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

    private void initViews() {
        adapter = new AccountsAdapter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void loadAccountList() {
        RealmManager rm = new RealmManager();
        rm.open();
        AccountDao ad = rm.createAccountDao();
        RealmResults<Account> accountsRealmResults = ad.getAll();
        // TODO may need to unregister listener onDestroyView
        accountsRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Account>>() {
            @Override
            public void onChange(RealmResults<Account> accounts) {
                updateRecyclerView(accounts);
            }
        });
        updateRecyclerView(accountsRealmResults);
    }

    private void updateRecyclerView(List<Account> accountsList) {
        if (adapter != null && accountsList != null) {
            adapter.setData(accountsList);
            adapter.notifyDataSetChanged();
        }

    }

    // TODO refactor
    public void onDeleteButtonClicked(final String accountName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.app_name);
        builder.setMessage("Are you sure you want to delete " + accountName + "?");
        builder.setIcon(R.drawable.ic_launcher_foreground);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                RealmManager rm = new RealmManager();
                rm.open();
                AccountDao ad = rm.createAccountDao();
                ad.deleteByName(accountName);
                rm.close();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

