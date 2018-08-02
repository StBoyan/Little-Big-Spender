package com.boyanstoynov.littlebigspender.main.accounts;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boyanstoynov.littlebigspender.BaseRecyclerAdapter;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Account;

import butterknife.BindView;

/**
 * RecyclerView adapter for Account entities.
 *
 * @author Boyan Stoynov
 */
public class AccountsAdapter extends BaseRecyclerAdapter<Account> {

    AccountsAdapter(RecyclerViewListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountViewHolder(parent, this);
    }

    public static class AccountViewHolder extends BaseRecyclerAdapter.ViewHolder<Account> {

        @BindView(R.id.text_itemAccount_account) TextView textAccount;
        @BindView(R.id.text_itemAccount_balance) TextView textBalance;
        @BindView(R.id.text_itemAccount_currency) TextView textCurrency;
        //TODO may have to remove. See Recurring and Transactions
        Context context;

        AccountViewHolder(ViewGroup parent, AccountsAdapter adapter) {
            super(parent, R.layout.item_account, adapter);

            context = parent.getContext();
        }

        @Override
        protected void setItemPresentation(Account account) {
            textAccount.setText(account.getName());
            textBalance.setText(account.getBalance().toString());

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            textCurrency.setText(prefs.getString("currencySymbol", "N/A"));
        }
    }
}
