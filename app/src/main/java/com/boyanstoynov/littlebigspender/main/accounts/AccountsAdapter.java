package com.boyanstoynov.littlebigspender.main.accounts;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Account;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Recycler View adapter to provide access to Account
 * entities and dynamically make View for each item.
 *
 * @author Boyan Stoynov
 */
public class AccountsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Account> accountsDataSet;
    private AccountsFragment accountsFragment;

    public AccountsAdapter(AccountsFragment fragment) {
        accountsDataSet = new ArrayList<>();
        accountsFragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);
        return new AccountViewHolder(view, accountsFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AccountViewHolder viewHolder = (AccountViewHolder) holder;
        Account acc = accountsDataSet.get(position);

        viewHolder.textAccount.setText(acc.getName());
        viewHolder.textBalance.setText(acc.getBalance().toString());
    }

    @Override
    public int getItemCount() {
        return accountsDataSet.size();
    }

    public void setData(@NonNull List<Account> accounts) {
        accountsDataSet.clear();
        accountsDataSet.addAll(accounts);
    }

    /**
     * Static inner class to describe how data in
     * each View is displayed.
     */
    public static class AccountViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_itemaccount_account) TextView textAccount;
        @BindView(R.id.text_itemaccount_balance) TextView textBalance;
        @BindView(R.id.button_itemaccount_delete) Button deleteButton;
        @BindView(R.id.button_itemaccount_edit) Button editButton;
        @BindView(R.id.divider_itemaccount) View divider;

        /* Whether view is clicked */
        boolean isExpanded;
        AccountsFragment fragment;

        public AccountViewHolder(View v, AccountsFragment fragment) {
            super(v);
            // Bind ButterKnife to View
            ButterKnife.bind(this, v);
            this.fragment = fragment;

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isExpanded) {
                        editButton.setVisibility(View.VISIBLE);
                        deleteButton.setVisibility(View.VISIBLE);
                        divider.setVisibility(View.VISIBLE);
                        isExpanded = true;
                    }
                    else {
                        editButton.setVisibility(View.GONE);
                        deleteButton.setVisibility(View.GONE);
                        divider.setVisibility(View.GONE);
                        isExpanded = false;
                    }
                }
            });
        }

        @OnClick(R.id.button_itemaccount_delete)
        public void onDeleteButtonClicked() {
            fragment.onDeleteButtonClicked(textAccount.getText().toString());
        }
    }
}
