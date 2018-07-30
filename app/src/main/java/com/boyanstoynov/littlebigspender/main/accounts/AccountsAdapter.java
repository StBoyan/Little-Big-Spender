package com.boyanstoynov.littlebigspender.main.accounts;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.boyanstoynov.littlebigspender.BaseRecyclerAdapter;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Account;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * RecyclerView adapter for Account entities.
 *
 * @author Boyan Stoynov
 */
public class AccountsAdapter extends BaseRecyclerAdapter<Account, AccountsFragment> {

    AccountsAdapter(AccountsFragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountViewHolder(parent, fragment);
    }

    public static class AccountViewHolder extends BaseRecyclerAdapter.ViewHolder<Account, AccountsFragment> {

        @BindView(R.id.text_itemaccount_account) TextView textAccount;
        @BindView(R.id.text_itemaccount_balance) TextView textBalance;
        @BindView(R.id.text_itemaccount_currency) TextView textCurrency;
        @BindView(R.id.button_itemaccount_delete) Button deleteButton;
        @BindView(R.id.button_itemaccount_edit) Button editButton;
        @BindView(R.id.divider_itemaccount) View divider;
        boolean isExpanded;

        AccountViewHolder(ViewGroup parent, AccountsFragment fragment) {
            super(parent, R.layout.item_account, fragment);

            itemView.setOnClickListener(new View.OnClickListener() {
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

        @Override
        protected void setItemPresentation(Account account) {
            textAccount.setText(account.getName());
            textBalance.setText(account.getBalance().toString());

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(fragment.getContext());
            textCurrency.setText(prefs.getString("currencySymbol", "N/A"));
        }

        @OnClick(R.id.button_itemaccount_delete)
        public void onDeleteButtonClicked() {
            fragment.onDeleteButtonClicked(item);
        }
    }
}
