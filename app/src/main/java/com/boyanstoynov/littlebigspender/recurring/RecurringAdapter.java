package com.boyanstoynov.littlebigspender.recurring;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.boyanstoynov.littlebigspender.BaseRecyclerAdapter;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Recurring;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * RecyclerView adapter for Recurring entities.
 *
 * @author Boyan Stoynov
 */
public class RecurringAdapter extends BaseRecyclerAdapter<Recurring, RecurringFragment> {

    RecurringAdapter(RecurringFragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecurringViewHolder(parent, fragment);
    }

    public static class RecurringViewHolder extends BaseRecyclerAdapter.ViewHolder<Recurring, RecurringFragment> {

        @BindView(R.id.text_itemrecurring_account) TextView textAccount;
        @BindView(R.id.text_itemrecurring_category) TextView textCategory;
        @BindView(R.id.text_itemrecurring_amount) TextView textAmount;
        @BindView(R.id.text_itemrecurring_date) TextView textDate;
        @BindView(R.id.text_itemrecurring_mode) TextView textMode;
        @BindView(R.id.text_itemrecurring_currency) TextView textCurrency;
        @BindView(R.id.button_itemrecurring_delete) Button deleteButton;
        @BindView(R.id.button_itemrecurring_edit) Button editButton;
        @BindView(R.id.divider_itemrecurring_lower) View divider;
        boolean isExpanded;

        RecurringViewHolder(ViewGroup parent, RecurringFragment fragment) {
            super(parent, R.layout.item_recurring, fragment);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isExpanded) {
                        editButton.setVisibility(View.VISIBLE);
                        deleteButton.setVisibility(View.VISIBLE);
                        divider.setVisibility(View.VISIBLE);
                        isExpanded = true;
                    } else {
                        editButton.setVisibility(View.GONE);
                        deleteButton.setVisibility(View.GONE);
                        divider.setVisibility(View.GONE);
                        isExpanded = false;
                    }
                }
            });
        }

        @Override
        protected void setItemPresentation(Recurring recurring) {
            //TODO refactor SimpleDateFormat here and in transactions. Maybe use a helper util class
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            textAccount.setText(recurring.getAccount().toString());
            textCategory.setText(recurring.getCategory().toString());
            textAmount.setText(recurring.getAmount().toString());
            textDate.setText(df.format(recurring.getStartDate()));
            textMode.setText(recurring.getMode().toString());

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(fragment.getContext());
            textCurrency.setText(prefs.getString("currencySymbol", "N/A"));
        }

        @OnClick(R.id.button_itemrecurring_delete)
        public void onDeleteButtonClicked() {
            fragment.onDeleteButtonClicked(item);
        }
    }
}
