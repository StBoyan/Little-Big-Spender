package com.boyanstoynov.littlebigspender.main.transactions;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.dao.AccountDao;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Recurring;
import com.boyanstoynov.littlebigspender.db.model.Transaction;
import com.boyanstoynov.littlebigspender.util.DateTimeUtils;
import com.boyanstoynov.littlebigspender.util.DecimalDigitsInputFilter;
import com.boyanstoynov.littlebigspender.util.SharedPrefsManager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.boyanstoynov.littlebigspender.main.overview.OverviewFragment.TAB_SELECT_KEY;
import static com.boyanstoynov.littlebigspender.util.Constants.CRYPTO_DIGITS_AFTER_ZERO_FILTER;
import static com.boyanstoynov.littlebigspender.util.Constants.CRYPTO_DIGITS_BEFORE_ZERO_FILTER;
import static com.boyanstoynov.littlebigspender.util.Constants.EXPENSE_POSITION;
import static com.boyanstoynov.littlebigspender.util.Constants.FIAT_DIGITS_AFTER_ZERO_FILTER;
import static com.boyanstoynov.littlebigspender.util.Constants.FIAT_DIGITS_BEFORE_ZERO_FILTER;
import static com.boyanstoynov.littlebigspender.util.Constants.INCOME_POSITION;
import static com.boyanstoynov.littlebigspender.util.Constants.MODE_BIWEEKLY_POSITION;
import static com.boyanstoynov.littlebigspender.util.Constants.MODE_MONTHLY_POSITION;
import static com.boyanstoynov.littlebigspender.util.Constants.MODE_WEEKLY_POSITION;

/**
 * Controller for Add transaction activity. Takes in user input
 * and validates it and creates new transactions and recurring
 * transactions.
 *
 * @author Boyan Stoynov
 */
public class AddTransactionActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.toolbar_addItem) Toolbar toolbar;
    @BindView(R.id.tabLayout_addTransaction) TabLayout tabLayout;
    @BindView(R.id.spinner_transaction_category) Spinner categorySpinner;
    @BindView(R.id.spinner_transaction_account) Spinner accountSpinner;
    @BindView(R.id.dateInput_transaction) EditText inputDate;
    @BindView(R.id.numberInput_transaction_amount) EditText inputAmount;
    @BindView(R.id.spinner_addTransaction_recurringMode) Spinner recurringModeSpinner;

    private AccountDao accountDao;
    private Date date;
    private List<Account> accountsList;
    private List<Category> expenseCategories;
    private List<Category> incomeCategories;
    private ArrayAdapter<Category> categoriesAdapter;
    private boolean isRecurring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        accountDao = getRealmManager().createAccountDao();

        expenseCategories = getRealmManager().createCategoryDao().getAllExpenseCategories();
        incomeCategories = getRealmManager().createCategoryDao().getAllIncomeCategories();
        categoriesAdapter = new ArrayAdapter<>(this, R.layout.item_spinner);
        categorySpinner.setAdapter(categoriesAdapter);

        accountsList = accountDao.getAll();
        ArrayAdapter<Account> accountsAdapter = new ArrayAdapter<>(
                this, R.layout.item_spinner, accountsList);
        accountSpinner.setAdapter(accountsAdapter);

        /* Get default account spinner selection and apply the appropriate
           decimal digits filter based on the account */
        setInputAmountFilter(0);

        setupTabSelectedListener();
        setUpAccountSpinnerListener();

        date = new Date();
        inputDate.setText(DateTimeUtils.formatDate(date));
        //Display first tab on create
        tabLayout.getTabAt(1).select();
        tabLayout.getTabAt(0).select();

        //If activity is started from overview screen, select tab and hide the TabLayout
        Bundle extraArgs = getIntent().getExtras();
        if (extraArgs != null) {
            tabLayout.getTabAt(extraArgs.getInt(TAB_SELECT_KEY)).select();
            tabLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_transaction;
    }

    /**
     * Validate input and create new transaction or recurring transaction
     * if checkbox is checked. If recurring transaction's date is today
     * also creates a new transaction.
     */
    @OnClick(R.id.button_addItem_add)
    public void addTransaction() {
        if (isAmountValid()) {
            if (isRecurring) {
                createRecurringTransaction();
                if (DateTimeUtils.isToday(date))
                    createTransaction();
            }
            else
                createTransaction();

            Toast.makeText(getApplicationContext(), "Transaction added", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    /**
     * Discard new transaction and go back.
     */
    @OnClick(R.id.button_addItem_cancel)
    public void cancelTransaction() {
        Toast.makeText(getApplicationContext(), "Transaction discarded", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @OnClick(R.id.dateInput_transaction)
    public void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, this, DateTimeUtils.yearToday(),
                DateTimeUtils.monthToday(), DateTimeUtils.dayToday());

        //Recurring transactions cannot have a date that is past today
        if (isRecurring)
            datePickerDialog.getDatePicker().setMinDate(new Date().getTime());

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = new GregorianCalendar(year, month, dayOfMonth).getTime();
        inputDate.setText(DateTimeUtils.formatDate(date));
    }

    /**
     * Hides/shows the recurring mode spinner upon clicking the recurring
     * checkbox. Also, if it's clicked into recurring position and currently
     * selected date is before today, set the date to day, since recurring
     * transactions cannot be made for dates that are passed.
     */
    @OnClick(R.id.checkbox_addTransaction_recurring)
    public void onCheckboxClicked() {
        isRecurring = !isRecurring;

        if (isRecurring && !DateTimeUtils.dateHasPassed(date)) {
            date = new Date();
            inputDate.setText(DateTimeUtils.formatDate(date));
        }

        if (isRecurring)
            recurringModeSpinner.setVisibility(View.VISIBLE);
        else
            recurringModeSpinner.setVisibility(View.GONE);
    }

    /**
     * Create new transaction and save it to database. Also,
     * subtract/add the transaction amount from/to account if
     * it is not in the future.
     */
    private void createTransaction() {
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

    /**
     * Create new recurring transaction and save it to database. Also,
     * calculate the next transaction date based on the start date.
     */
    private void createRecurringTransaction() {
        Recurring newRecurring = new Recurring();
        newRecurring.setAccount((Account) accountSpinner.getSelectedItem());
        newRecurring.setCategory((Category) categorySpinner.getSelectedItem());
        newRecurring.setAmount(new BigDecimal(inputAmount.getText().toString()));
        newRecurring.setStartDate(date);

        Date nextTransactionDate = date;
        switch (recurringModeSpinner.getSelectedItemPosition()) {
            case MODE_MONTHLY_POSITION:
                newRecurring.setMode(Recurring.Mode.MONTHLY);
                nextTransactionDate = DateTimeUtils.addMonth(nextTransactionDate);
                break;
            case MODE_BIWEEKLY_POSITION:
                newRecurring.setMode(Recurring.Mode.BIWEEKLY);
                nextTransactionDate = DateTimeUtils.addTwoWeeks(nextTransactionDate);
                break;
            case MODE_WEEKLY_POSITION:
                newRecurring.setMode(Recurring.Mode.WEEKLY);
                nextTransactionDate = DateTimeUtils.addWeek(nextTransactionDate);
                break;
        }
        newRecurring.setNextTransactionDate(nextTransactionDate);

        getRealmManager().createRecurringDao().save(newRecurring);
    }

    /**
     * Set up account spinner listener that listens for change in selection
     * and applies the appropriate decimal digit filter.
     */
    private void setUpAccountSpinnerListener() {
        accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setInputAmountFilter(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**
     * Set up tab layout listener that listens for changes in the selected
     * tab and changes the categories displayed in the categories spinner.
     */
    private void setupTabSelectedListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                categoriesAdapter.clear();
                switch (tab.getPosition()) {
                    case INCOME_POSITION:
                        categoriesAdapter.addAll(incomeCategories);
                        break;
                    case EXPENSE_POSITION:
                        categoriesAdapter.addAll(expenseCategories);
                        break;
                }
                categoriesAdapter.notifyDataSetChanged();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    /**
     * Set the input amount decimal digits filter based on the type
     * of account, either crypto or fiat and clear amount input text
     * field.
     * @param position position of account within the accountList
     */
    private void setInputAmountFilter(int position) {
        if (accountsList.get(position).isCrypto()) {
            inputAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(
                    CRYPTO_DIGITS_BEFORE_ZERO_FILTER, CRYPTO_DIGITS_AFTER_ZERO_FILTER)});
            inputAmount.setText("");
        }
        else {
            inputAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(
                    FIAT_DIGITS_BEFORE_ZERO_FILTER, FIAT_DIGITS_AFTER_ZERO_FILTER)});
            inputAmount.setText("");
        }
    }

    /**
     * Checks amount user input and returns boolean whether it is
     * valid or not. If invalid displays error message to user.
     * @return boolean whether amount is valid
     */
    private boolean isAmountValid() {
        if (inputAmount.getText().toString().equals("")) {
            inputAmount.setError(getResources().getString(R.string.all_blank_field_error));
            return false;
        }

        BigDecimal transactionAmount = new BigDecimal(inputAmount.getText().toString());

        if (transactionAmount.compareTo(BigDecimal.ZERO) < 1) {
            inputAmount.setError(getResources().getString(R.string.all_cannot_be_zero_error));
            return false;
        }

        if (tabLayout.getSelectedTabPosition() == EXPENSE_POSITION) {
            BigDecimal accountBalance = new BigDecimal(
                    accountsList.get(accountSpinner.getSelectedItemPosition()).getBalance().toString());
        /* Check if transaction will bring cryptocurrency below 0
           which is not allowed. */
            if (accountsList.get(accountSpinner.getSelectedItemPosition()).isCrypto()) {
                if (transactionAmount.compareTo(accountBalance) > 0) {
                    inputAmount.setError(getResources().getString(R.string.transaction_crypto_negative_balance_error));
                    return false;
                }
            }

        /* Check if user allows transactions to bring account to
           negative (go into overdraft). Only for fiat currencies.
           Cryptocurrencies cannot be negative */
            if (!SharedPrefsManager.read(
                    getResources().getString(R.string.allowTransactionOverdraft), false)) {
                if (transactionAmount.compareTo(accountBalance) > 0) {
                    inputAmount.setError(getResources().getString(R.string.transaction_fiat_negative_balance_error));
                    return false;
                }
            }
        }

        return true;
    }
}
