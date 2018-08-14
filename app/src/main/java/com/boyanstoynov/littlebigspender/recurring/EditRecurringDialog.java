package com.boyanstoynov.littlebigspender.recurring;

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
import com.boyanstoynov.littlebigspender.db.model.Recurring;
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
import static com.boyanstoynov.littlebigspender.util.Constants.MODE_BIWEEKLY_POSITION;
import static com.boyanstoynov.littlebigspender.util.Constants.MODE_MONTHLY_POSITION;
import static com.boyanstoynov.littlebigspender.util.Constants.MODE_WEEKLY_POSITION;

/**
 * Edit recurring transaction dialog implementation. Handles changing
 * recurring transaction fields and validating user input.
 *
 * @author Boyan Stoynov
 */
public class EditRecurringDialog extends BaseEditorDialog<Recurring> implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.spinner_transaction_category) Spinner categorySpinner;
    @BindView(R.id.spinner_transaction_account) Spinner accountSpinner;
    @BindView(R.id.numberInput_transaction_amount) EditText amountInput;
    @BindView(R.id.dateInput_transaction) EditText dateInput;
    @BindView(R.id.spinner_dialogRecurring_mode) Spinner modeSpinner;

    private Date date;
    private List<Account> accountsList;
    private List<Category> categoriesList;

    @Override
    protected int getTitleResource() {
        return R.string.recurring_editDialog_title;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_recurring_edit;
    }

    @Override
    protected boolean onPositiveClick() {
        if (isAmountValid()) {
            item.setCategory((Category) categorySpinner.getSelectedItem());
            item.setAccount((Account) accountSpinner.getSelectedItem());
            item.setAmount(new BigDecimal(amountInput.getText().toString()));
            item.setStartDate(date);

            switch (modeSpinner.getSelectedItemPosition()) {
                case MODE_MONTHLY_POSITION:
                    item.setMode(Recurring.Mode.MONTHLY);
                    break;
                case MODE_BIWEEKLY_POSITION:
                    item.setMode(Recurring.Mode.BIWEEKLY);
                    break;
                case MODE_WEEKLY_POSITION:
                    item.setMode(Recurring.Mode.WEEKLY);
                    break;
            }
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
        // Set current category selection to the recurring transaction's category
        for (int i = 0; i<categoriesAdapter.getCount(); i++) {
            if (categoriesAdapter.getItem(i).getName().equals(item.getCategory().getName()))
                categorySpinner.setSelection(i);
        }

        accountSpinner.setAdapter(accountsAdapter);
        // Set current account selection to the recurring transaction's account
        for (int i = 0; i<accountsAdapter.getCount(); i++) {
            if (accountsAdapter.getItem(i).getName().equals(item.getAccount().getName()))
                accountSpinner.setSelection(i);
        }

        boolean isCrypto = item.getAccount().isCrypto();
        // Disable account spinner for cryptocurrency accounts
        if (isCrypto)
            accountSpinner.setEnabled(false);

        /*Apply appropriate formatting and input filter based on whether the
        Recurring transaction is crypto or not */
        if (isCrypto) {
            amountInput.setText(getCryptoFormatter().format(item.getAmount()));
            amountInput.setFilters(new InputFilter[] {
                    new DecimalDigitsInputFilter(
                            CRYPTO_DIGITS_BEFORE_ZERO_FILTER, CRYPTO_DIGITS_AFTER_ZERO_FILTER)});
        }
        else {
            amountInput.setText(getFiatFormatter().format(item.getAmount()));
            amountInput.setFilters(new InputFilter[] {
                    new DecimalDigitsInputFilter(
                            FIAT_DIGITS_BEFORE_ZERO_FILTER, FIAT_DIGITS_AFTER_ZERO_FILTER)});
        }

        date = item.getStartDate();
        dateInput.setText(DateTimeUtils.formatDate(date));
        switch (item.getMode()) {
            case MONTHLY:
                modeSpinner.setSelection(MODE_MONTHLY_POSITION);
                break;
            case BIWEEKLY:
                modeSpinner.setSelection(MODE_BIWEEKLY_POSITION);
                break;
            case WEEKLY:
                modeSpinner.setSelection(MODE_WEEKLY_POSITION);
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = new GregorianCalendar(year, month, dayOfMonth).getTime();
        dateInput.setText(DateTimeUtils.formatDate(date));
    }

    @OnClick(R.id.dateInput_transaction)
    public void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(), this, DateTimeUtils.yearToday(),
                DateTimeUtils.monthToday(), DateTimeUtils.dayToday());
        datePickerDialog.getDatePicker().setMinDate(date.getTime());
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
