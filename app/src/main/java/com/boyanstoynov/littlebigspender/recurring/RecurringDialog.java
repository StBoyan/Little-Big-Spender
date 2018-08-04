package com.boyanstoynov.littlebigspender.recurring;

import android.app.DatePickerDialog;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.boyanstoynov.littlebigspender.BaseEditorDialog;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Recurring;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Edit recurring transaction dialog implementation.
 *
 * @author Boyan Stoynov
 */
public class RecurringDialog extends BaseEditorDialog<Recurring> implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.spinner_transaction_category) Spinner categorySpinner;
    @BindView(R.id.spinner_transaction_account) Spinner accountSpinner;
    @BindView(R.id.numberInput_transaction_amount) EditText amountInput;
    @BindView(R.id.dateInput_transaction) EditText dateInput;
    @BindView(R.id.spinner_dialogRecurring_mode) Spinner modeSpinner;

    Date date;
    List<Account> accountsList;
    List<Category> categoriesList;


    @Override
    protected int getTitleResource() {
        return R.string.recurring_editDialog_title;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_recurring;
    }

    @Override
    protected boolean onPositiveClick() {
        item.setCategory((Category) categorySpinner.getSelectedItem());
        item.setAccount((Account) accountSpinner.getSelectedItem());
        item.setAmount(new BigDecimal(amountInput.getText().toString()));
        item.setStartDate(date);

        //TODO remove magic numbers
        switch (modeSpinner.getSelectedItemPosition()) {
            case 0:
                item.setMode(Recurring.Mode.MONTHLY);
                break;
            case 1:
                item.setMode(Recurring.Mode.BIWEEKLY);
                break;
            case 2:
                item.setMode(Recurring.Mode.WEEKLY);
                break;
        }

        //TODO implement validation
        return true;
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

        for (int i =0; i<categoriesAdapter.getCount(); i++) {
            if (categoriesAdapter.getItem(i).getName().equals(item.getCategory().getName()))
                categorySpinner.setSelection(i);
        }

        accountSpinner.setAdapter(accountsAdapter);

        for (int i =0; i<accountsAdapter.getCount(); i++) {
            if (accountsAdapter.getItem(i).getName().equals(item.getAccount().getName()))
                accountSpinner.setSelection(i);
        }

        amountInput.setText(item.getAmount().toString());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        date = item.getStartDate();
        dateInput.setText(df.format(item.getStartDate()));
        //TODO remove magic numbers
        switch (item.getMode()) {
            case MONTHLY:
                modeSpinner.setSelection(0);
                break;
            case BIWEEKLY:
                modeSpinner.setSelection(1);
                break;
            case WEEKLY:
                modeSpinner.setSelection(2);
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        date = new GregorianCalendar(year, month, dayOfMonth).getTime();
        dateInput.setText(df.format(date));
    }

    @OnClick(R.id.dateInput_transaction)
    public void showDatePicker() {
        Calendar calendar = new GregorianCalendar(Locale.getDefault());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), this, year, month, day);
        //TODO replace some date functionality with date util class
        datePickerDialog.show();
    }

    public void setAccountsList(List<Account> accountsList) {
        this.accountsList = new ArrayList<>(accountsList);
    }

    public void setCategoriesList(List<Category> categoriesList) {
        this.categoriesList = new ArrayList<>(categoriesList);
    }
}
