package com.boyanstoynov.littlebigspender.main.transactions;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boyanstoynov.littlebigspender.BaseRecyclerAdapter;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Transaction;
import com.boyanstoynov.littlebigspender.util.DateTimeUtils;
import com.boyanstoynov.littlebigspender.util.SharedPrefsManager;

import java.math.BigDecimal;

import butterknife.BindView;

import static com.boyanstoynov.littlebigspender.util.Constants.CRYPTO_ACCOUNT_VIEW_TYPE;
import static com.boyanstoynov.littlebigspender.util.Constants.FIAT_ACCOUNT_VIEW_TYPE;

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
        if (viewType == FIAT_ACCOUNT_VIEW_TYPE)
            return new TransactionViewHolder(parent, this);
        else
            return new CryptoTransactionViewHolder(parent, this);
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).getAccount()
                .isCrypto() ? CRYPTO_ACCOUNT_VIEW_TYPE : FIAT_ACCOUNT_VIEW_TYPE;
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
            textAccount.setText(transaction.getAccount().getName());
            textCategory.setText(transaction.getCategory().getName());
            textAmount.setText(getFiatFormatter().format(transaction.getAmount()));
            textDate.setText(DateTimeUtils.formatDate(transaction.getDate()));

            if (transaction.getCategory().getType() == Category.Type.INCOME)
                textType.setText(R.string.all_plus_symbol);
            else
                textType.setText(R.string.all_minus_symbol);

            textCurrency.setText(SharedPrefsManager.getCurrencySymbol());
        }
    }

    public static class CryptoTransactionViewHolder extends BaseRecyclerAdapter.ViewHolder<Transaction> {

        @BindView(R.id.text_itemTransaction_account) TextView textAccount;
        @BindView(R.id.text_itemTransaction_category) TextView textCategory;
        @BindView(R.id.text_itemTransaction_cryptoAmount) TextView textCryptoAmount;
        @BindView(R.id.text_itemTransaction_fiatAmount) TextView textFiatAmount;
        @BindView(R.id.text_itemTransaction_date) TextView textDate;
        @BindView(R.id.text_itemTransaction_currency) TextView textCurrency;
        @BindView(R.id.text_itemTransaction_type) TextView textType;

        CryptoTransactionViewHolder(ViewGroup parent, BaseRecyclerAdapter adapter) {
            super(parent, R.layout.item_crypto_transaction, adapter);
        }

        @Override
        protected void setItemPresentation(Transaction transaction) {
            textAccount.setText(transaction.getAccount().toString());
            textCategory.setText(transaction.getCategory().toString());
            textCryptoAmount.setText(getCryptoFormatter().format(transaction.getAmount()));
            textDate.setText(DateTimeUtils.formatDate(transaction.getDate()));
            textCurrency.setText(SharedPrefsManager.getCurrencySymbol());

            if (transaction.getCategory().getType() == Category.Type.INCOME)
                textType.setText(R.string.all_plus_symbol);
            else
                textType.setText(R.string.all_minus_symbol);

            BigDecimal fiatValue = transaction.getAccount().getFiatValue();

            //Display fiat value if converted rated has been fetched
            if (fiatValue.doubleValue() != 0.0)
                textFiatAmount.setText(getFiatFormatter().format(
                        transaction.getAmount().multiply(fiatValue)));
            else
                textFiatAmount.setText(R.string.all_not_available);
        }
    }
}
