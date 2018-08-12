package com.boyanstoynov.littlebigspender.main.transactions;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.db.dao.AccountDao;
import com.boyanstoynov.littlebigspender.db.dao.CategoryDao;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Recurring;
import com.boyanstoynov.littlebigspender.db.model.Transaction;
import com.boyanstoynov.littlebigspender.util.DateTimeUtils;
import com.boyanstoynov.littlebigspender.util.DecimalDigitsInputFilter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.RealmResults;

import static com.boyanstoynov.littlebigspender.util.Constants.FIAT_DIGITS_AFTER_ZERO_FILTER;
import static com.boyanstoynov.littlebigspender.util.Constants.FIAT_DIGITS_BEFORE_ZERO_FILTER;
import static com.boyanstoynov.littlebigspender.util.Constants.MODE_BIWEEKLY_POSITION;
import static com.boyanstoynov.littlebigspender.util.Constants.MODE_MONTHLY_POSITION;
import static com.boyanstoynov.littlebigspender.util.Constants.MODE_WEEKLY_POSITION;

/**
 * Controller for Add transaction activity.
 *
 * @author Boyan Stoynov
 */
public class AddTransactionActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.toolbar_addTransaction) Toolbar toolbar;
    @BindView(R.id.tabLayout_addTransaction) TabLayout tabLayout;
    @BindView(R.id.spinner_transaction_category) Spinner categorySpinner;
    @BindView(R.id.spinner_transaction_account) Spinner accountSpinner;
    @BindView(R.id.dateInput_transaction) EditText inputDate;
    @BindView(R.id.numberInput_transaction_amount) EditText inputAmount;
    @BindView(R.id.spinner_addTransaction_recurringMode) Spinner recurringModeSpinner;

    CategoryDao categoryDao;
    AccountDao accountDao;
    Date date;
    boolean isRecurring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        inputAmount.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(FIAT_DIGITS_BEFORE_ZERO_FILTER, FIAT_DIGITS_AFTER_ZERO_FILTER)});

        categoryDao = getRealmManager().createCategoryDao();
        final RealmResults<Category> expenseCategories = categoryDao.getAllExpenseCategories();
        final RealmResults<Category> incomeCategories = categoryDao.getAllIncomeCategories();

        final ArrayAdapter<Category> categoriesAdapter = new ArrayAdapter<>(this, R.layout.item_spinner);
        categorySpinner.setAdapter(categoriesAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                categoriesAdapter.clear();
                switch (tab.getPosition()) {
                    case 0:
                        categoriesAdapter.addAll(incomeCategories);
                        break;
                    case 1:
                        categoriesAdapter.addAll(expenseCategories);
                        break;
                }
                categoriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        accountDao = getRealmManager().createAccountDao();
        RealmResults<Account> accounts = accountDao.getAll();
        ArrayAdapter<Account> accountsAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, accounts);
        accountSpinner.setAdapter(accountsAdapter);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        date = new Date();
        inputDate.setText(df.format(date));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_transaction;
    }
    @OnClick(R.id.button_addTransaction_add)
    public void addTransaction() {
        // TODO need to do validation here
        createTransaction();
        Toast.makeText(getApplicationContext(), "Transaction added", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @OnClick(R.id.button_addTransaction_cancel)
    public void cancelTransaction() {
        Toast.makeText(getApplicationContext(), "Transaction discarded", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @OnClick(R.id.dateInput_transaction)
    public void showDatePicker() {
        Calendar calendar = new GregorianCalendar(Locale.getDefault());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);

        if (isRecurring)
            datePickerDialog.getDatePicker().setMinDate(new Date().getTime());

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        date = new GregorianCalendar(year, month, dayOfMonth).getTime();
        inputDate.setText(df.format(date));
    }

    @OnClick(R.id.checkbox_addTransaction_recurring)
    public void onCheckboxClicked() {
        isRecurring = !isRecurring;

        if (isRecurring && date.compareTo(new Date()) < 0) {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            date = new Date();
            inputDate.setText(df.format(date));
        }
        // TODO better combine with previous if
        if (isRecurring)
            recurringModeSpinner.setVisibility(View.VISIBLE);
        else
            recurringModeSpinner.setVisibility(View.GONE);
    }

    public void createTransaction() {
        if (!isRecurring) {
            Transaction newTransaction = new Transaction();
            Account transactionAccount = (Account) accountSpinner.getSelectedItem();
            BigDecimal transactionAmount = new BigDecimal(inputAmount.getText().toString());
            Category transactionCategory = (Category) categorySpinner.getSelectedItem();
            newTransaction.setAccount(transactionAccount);
            newTransaction.setCategory(transactionCategory);
            newTransaction.setAmount(transactionAmount);
            newTransaction.setDate(date);

            if (transactionCategory.getType() == Category.Type.INCOME)
                accountDao.addAmount(transactionAccount, transactionAmount);
            else
                accountDao.subtractAmount(transactionAccount, transactionAmount);

            getRealmManager().createTransactionDao().save(newTransaction);
        }
        else {
            Recurring newRecurring = new Recurring();
            newRecurring.setAccount((Account) accountSpinner.getSelectedItem());
            newRecurring.setCategory((Category) categorySpinner.getSelectedItem());
            newRecurring.setAmount(new BigDecimal(inputAmount.getText().toString()));
            newRecurring.setStartDate(date);
            switch (recurringModeSpinner.getSelectedItemPosition()) {
                case MODE_MONTHLY_POSITION:
                    newRecurring.setMode(Recurring.Mode.MONTHLY);
                    break;
                case MODE_BIWEEKLY_POSITION:
                    newRecurring.setMode(Recurring.Mode.BIWEEKLY);
                    break;
                case MODE_WEEKLY_POSITION:
                    newRecurring.setMode(Recurring.Mode.WEEKLY);
                    break;
            }

            //TODO temporary solution below. need to implement smart next transaction calculation
            newRecurring.setNextTransactionDate(new Date());
            getRealmManager().createRecurringDao().save(newRecurring);

            if (DateTimeUtils.isToday(date)) {
                Transaction newTransaction = new Transaction();
                newTransaction.setAccount((Account) accountSpinner.getSelectedItem());
                newTransaction.setCategory((Category) categorySpinner.getSelectedItem());
                newTransaction.setAmount(new BigDecimal(inputAmount.getText().toString()));
                newTransaction.setDate(date);

                getRealmManager().createTransactionDao().save(newTransaction);
            }
        }
    }
 }
