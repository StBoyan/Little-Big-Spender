package com.boyanstoynov.littlebigspender.main.accounts;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boyanstoynov.littlebigspender.BaseRecyclerAdapter;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.util.SharedPreferencesManager;

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

        AccountViewHolder(ViewGroup parent, AccountsAdapter adapter) {
            super(parent, R.layout.item_account, adapter);
        }

        @Override
        protected void setItemPresentation(Account account) {
            textAccount.setText(account.getName());
            textBalance.setText(getMoneyFormatter().format(account.getBalance()));
            textCurrency.setText(SharedPreferencesManager.getCurrencySymbol());
        }
    }
}
