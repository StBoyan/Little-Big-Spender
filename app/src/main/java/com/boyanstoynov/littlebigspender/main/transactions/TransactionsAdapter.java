package com.boyanstoynov.littlebigspender.main.transactions;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boyanstoynov.littlebigspender.BaseRecyclerAdapter;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Transaction;
import com.boyanstoynov.littlebigspender.util.SharedPreferencesManager;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;

/**
 * RecyclerView adapter for Transaction entities.
 *
 * @author Boyan Stoynov
 */
public class TransactionsAdapter extends BaseRecyclerAdapter<Transaction>{

    TransactionsAdapter(RecyclerViewListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(parent, this);
    }

    public static class TransactionViewHolder extends BaseRecyclerAdapter.ViewHolder<Transaction> {

        @BindView(R.id.text_itemTransaction_account) TextView textAccount;
        @BindView(R.id.text_itemTransaction_category) TextView textCategory;
        @BindView(R.id.text_itemTransaction_amount) TextView textAmount;
        @BindView(R.id.text_itemTransaction_date) TextView textDate;
        @BindView(R.id.text_itemTransaction_currency) TextView textCurrency;
        @BindView(R.id.text_itemTransaction_type) TextView textType;

        TransactionViewHolder(ViewGroup parent, TransactionsAdapter adapter) {
            super(parent, R.layout.item_transaction, adapter);
        }

        @Override
        protected void setItemPresentation(Transaction transaction) {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            textAccount.setText(transaction.getAccount().getName());
            textCategory.setText(transaction.getCategory().getName());
            textAmount.setText(transaction.getAmount().toString());
            textDate.setText(df.format(transaction.getDate()));

            if (transaction.getCategory().getType() == Category.Type.INCOME)
                textType.setText(R.string.all_plus_symbol);
            else
                textType.setText(R.string.all_minus_symbol);

            textCurrency.setText(SharedPreferencesManager.getCurrencySymbol());
        }
    }
}
