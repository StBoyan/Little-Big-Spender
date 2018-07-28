package com.boyanstoynov.littlebigspender.main.transactions;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.boyanstoynov.littlebigspender.BaseRecyclerAdapter;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * RecyclerView adapter for Transaction entities.
 *
 * @author Boyan Stoynov
 */
public class TransactionsAdapter extends BaseRecyclerAdapter<Transaction, TransactionsFragment>{

    TransactionsAdapter(TransactionsFragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(parent, fragment);
    }

    public static class TransactionViewHolder extends BaseRecyclerAdapter.ViewHolder<Transaction, TransactionsFragment> {

        @BindView(R.id.text_itemtransaction_account) TextView textAccount;
        @BindView(R.id.text_itemtransaction_category) TextView textCategory;
        @BindView(R.id.text_itemtransaction_amount) TextView textAmount;
        @BindView(R.id.text_itemtransaction_date) TextView textDate;
        @BindView(R.id.button_itemtransaction_delete) Button deleteButton;
        @BindView(R.id.button_itemtransaction_edit) Button editButton;
        @BindView(R.id.divider_itemtransaction_lower) View divider;
        private boolean isExpanded;

        TransactionViewHolder(ViewGroup parent, TransactionsFragment fragment) {
            super(parent, R.layout.item_transaction, fragment);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isExpanded) {
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
        protected void setItemPresentation(Transaction transaction) {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            textAccount.setText(transaction.getAccount().getName());
            textCategory.setText(transaction.getCategory().getName());
            textAmount.setText(transaction.getAmount().toString());
            textDate.setText(df.format(transaction.getDate()));
        }

        @OnClick(R.id.button_itemtransaction_delete)
        public void onDeleteButtonClicked() {
            fragment.onDeleteButtonClicked(item);
        }
    }
}
