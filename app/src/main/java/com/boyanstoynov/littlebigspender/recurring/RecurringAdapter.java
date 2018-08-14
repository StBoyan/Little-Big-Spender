package com.boyanstoynov.littlebigspender.recurring;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boyanstoynov.littlebigspender.BaseRecyclerAdapter;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Recurring;
import com.boyanstoynov.littlebigspender.util.DateTimeUtils;
import com.boyanstoynov.littlebigspender.util.SharedPrefsManager;

import java.math.BigDecimal;

import butterknife.BindView;

import static com.boyanstoynov.littlebigspender.util.Constants.CRYPTO_ACCOUNT_VIEW_TYPE;
import static com.boyanstoynov.littlebigspender.util.Constants.FIAT_ACCOUNT_VIEW_TYPE;

/**
 * RecyclerView adapter for Recurring entities.
 *
 * @author Boyan Stoynov
 */
public class RecurringAdapter extends BaseRecyclerAdapter<Recurring> {

    RecurringAdapter(RecyclerViewListener<Recurring> listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == FIAT_ACCOUNT_VIEW_TYPE)
            return new RecurringViewHolder(parent, this);
        else
            return new CryptoRecurringViewHolder(parent, this);
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).getAccount()
                .isCrypto() ? CRYPTO_ACCOUNT_VIEW_TYPE : FIAT_ACCOUNT_VIEW_TYPE;
    }

    public static class RecurringViewHolder extends BaseRecyclerAdapter.ViewHolder<Recurring> {

        @BindView(R.id.text_itemRecurring_account) TextView textAccount;
        @BindView(R.id.text_itemRecurring_category) TextView textCategory;
        @BindView(R.id.text_itemRecurring_amount) TextView textAmount;
        @BindView(R.id.text_itemRecurring_date) TextView textDate;
        @BindView(R.id.text_itemRecurring_mode) TextView textMode;
        @BindView(R.id.text_itemRecurring_currency) TextView textCurrency;

        RecurringViewHolder(ViewGroup parent, RecurringAdapter adapter) {
            super(parent, R.layout.item_recurring, adapter);
        }

        @Override
        protected void setItemPresentation(Recurring recurring) {
            textAccount.setText(recurring.getAccount().toString());
            textCategory.setText(recurring.getCategory().toString());
            textAmount.setText(getFiatFormatter().format(recurring.getAmount()));
            textDate.setText(DateTimeUtils.formatDate(recurring.getStartDate()));
            textMode.setText(recurring.getMode().toString());
            textCurrency.setText(SharedPrefsManager.getCurrencySymbol());
        }
    }

    public static class CryptoRecurringViewHolder extends BaseRecyclerAdapter.ViewHolder<Recurring> {

        @BindView(R.id.text_itemRecurring_account) TextView textAccount;
        @BindView(R.id.text_itemRecurring_category) TextView textCategory;
        @BindView(R.id.text_itemRecurring_cryptoAmount) TextView textCryptoAmount;
        @BindView(R.id.text_itemRecurring_fiatAmount) TextView textFiatAmount;
        @BindView(R.id.text_itemRecurring_date) TextView textDate;
        @BindView(R.id.text_itemRecurring_mode) TextView textMode;
        @BindView(R.id.text_itemRecurring_currency) TextView textCurrency;

        CryptoRecurringViewHolder(ViewGroup parent, BaseRecyclerAdapter adapter) {
            super(parent, R.layout.item_crypto_recurring, adapter);
        }

        @Override
        protected void setItemPresentation(Recurring recurring) {
            textAccount.setText(recurring.getAccount().toString());
            textMode.setText(recurring.getMode().toString());
            textCategory.setText(recurring.getCategory().toString());
            textCryptoAmount.setText(getCryptoFormatter().format(recurring.getAmount()));
            textCurrency.setText(SharedPrefsManager.getCurrencySymbol());
            textDate.setText(DateTimeUtils.formatDate(recurring.getStartDate()));

            BigDecimal fiatValue = recurring.getAccount().getFiatValue();

            // Display fiat value if converted rate has been fetched
            if (fiatValue.doubleValue() != 0.0)
                textFiatAmount.setText(getFiatFormatter().format(
                        recurring.getAmount().multiply(fiatValue)));
            else
                textFiatAmount.setText(R.string.all_not_available);
        }
    }
}
