package com.boyanstoynov.littlebigspender.recurring;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boyanstoynov.littlebigspender.BaseRecyclerAdapter;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Recurring;
import com.boyanstoynov.littlebigspender.util.SharedPreferencesManager;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;

/**
 * RecyclerView adapter for Recurring entities.
 *
 * @author Boyan Stoynov
 */
public class RecurringAdapter extends BaseRecyclerAdapter<Recurring> {

    RecurringAdapter(RecyclerViewListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecurringViewHolder(parent, this);
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
            //TODO refactor SimpleDateFormat here and in transactions. Maybe use a helper util class
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            textAccount.setText(recurring.getAccount().toString());
            textCategory.setText(recurring.getCategory().toString());
            textAmount.setText(getMoneyFormatter().format(recurring.getAmount()));
            textDate.setText(df.format(recurring.getStartDate()));
            textMode.setText(recurring.getMode().toString());
            textCurrency.setText(SharedPreferencesManager.getCurrencySymbol());
        }
    }
}
