package com.boyanstoynov.littlebigspender.main.transactions;

import android.app.DatePickerDialog;
import android.text.InputFilter;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.boyanstoynov.littlebigspender.BaseEditorDialog;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Transaction;
import com.boyanstoynov.littlebigspender.util.DateTimeUtils;
import com.boyanstoynov.littlebigspender.util.DecimalDigitsInputFilter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.boyanstoynov.littlebigspender.util.Constants.CRYPTO_DIGITS_AFTER_ZERO_FILTER;
import static com.boyanstoynov.littlebigspender.util.Constants.CRYPTO_DIGITS_BEFORE_ZERO_FILTER;
import static com.boyanstoynov.littlebigspender.util.Constants.FIAT_DIGITS_AFTER_ZERO_FILTER;
import static com.boyanstoynov.littlebigspender.util.Constants.FIAT_DIGITS_BEFORE_ZERO_FILTER;

/**
 * Edit transaction dialog implementation. Handles changing
 * transaction fields and validating user input. Provides callback
 * upon user confirmation.
 *
 * @author Boyan Stoynov
 */
public class EditTransactionDialog extends BaseEditorDialog<Transaction> implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.spinner_transaction_category) Spinner categorySpinner;
    @BindView(R.id.spinner_transaction_account) Spinner accountSpinner;
    @BindView(R.id.numberInput_transaction_amount) EditText amountInput;
    @BindView(R.id.dateInput_transaction) EditText dateInput;

    private Date date;
    private List<Account> accountsList;
    private List<Category> categoriesList;

    @Override
    protected int getTitleResource() {
        return R.string.transaction_editDialog_title;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_transaction_edit;
    }

    @Override
    protected boolean onPositiveClick() {
        if (isAmountValid()) {
            item.setCategory((Category) categorySpinner.getSelectedItem());
            item.setAccount((Account) accountSpinner.getSelectedItem());
            item.setAmount(new BigDecimal(amountInput.getText().toString()));
            item.setDate(date);
            return true;
        }
        else
            return false;
    }

    @Override
    protected void populateDialog() {
        if (accountsList == null)
            throw new IllegalStateException("Accounts not set. Need to call setAccountsList() before show().");
        if (categoriesList == null)
            throw new IllegalStateException("Categories not set. Need to call setCategoriesList() before show().");

        ArrayAdapter<Account> accountsAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, accountsList);
        ArrayAdapter<Category> categoriesAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, categoriesList);

        categorySpinner.setAdapter(categoriesAdapter);
        // Set spinner to item's category position
        for (int i =0; i<categoriesAdapter.getCount(); i++) {
            if (categoriesAdapter.getItem(i).getName().equals(item.getCategory().getName()))
                categorySpinner.setSelection(i);
        }

        accountSpinner.setAdapter(accountsAdapter);
        // Set spinner to item's account position
        for (int i =0; i<accountsAdapter.getCount(); i++) {
            if (accountsAdapter.getItem(i).getName().equals(item.getAccount().getName()))
                accountSpinner.setSelection(i);
        }

        if (item.getAccount().isCrypto()) {
            /* Disable account spinner if crypto since crypto transactions
               cannot change account */
            accountSpinner.setEnabled(false);
            amountInput.setText(getCryptoFormatter().format(item.getAmount()));
            amountInput.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(
                    CRYPTO_DIGITS_BEFORE_ZERO_FILTER, CRYPTO_DIGITS_AFTER_ZERO_FILTER)});
        }
        else {
            amountInput.setText(getFiatFormatter().format(item.getAmount()));
            amountInput.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(FIAT_DIGITS_BEFORE_ZERO_FILTER,
                    FIAT_DIGITS_AFTER_ZERO_FILTER)});
        }

        date = item.getDate();
        dateInput.setText(DateTimeUtils.formatDate(item.getDate()));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = new GregorianCalendar(year, month, dayOfMonth).getTime();
        dateInput.setText(DateTimeUtils.formatDate(date));
}

    @OnClick(R.id.dateInput_transaction)
    public void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), this,
                DateTimeUtils.yearToday(), DateTimeUtils.monthToday(), DateTimeUtils.monthToday());
        datePickerDialog.show();
    }

    public void setAccountsList(List<Account> accountsList) {
        this.accountsList = new ArrayList<>(accountsList);
    }

    public void setCategoriesList(List<Category> categoriesList) {
        this.categoriesList = new ArrayList<>(categoriesList);
    }

    /**
     * Checks amount user input and returns boolean whether it is
     * valid or not. If invalid displays error message to user.
     * @return boolean whether amount is valid
     */
    private boolean isAmountValid() {
        if (amountInput.getText().toString().equals("")) {
            amountInput.setError(getResources().getString(R.string.all_blank_field_error));
            return false;
        }

        if (new BigDecimal(amountInput.getText().toString()).compareTo(BigDecimal.ZERO) < 1) {
            amountInput.setError(getResources().getString(R.string.all_cannot_be_zero_error));
            return false;
        }

        return true;
    }
}
