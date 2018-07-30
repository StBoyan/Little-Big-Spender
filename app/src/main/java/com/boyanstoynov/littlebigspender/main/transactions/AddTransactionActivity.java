package com.boyanstoynov.littlebigspender.main.transactions;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.RealmResults;

/**
 * Controller for Add transaction activity.
 *
 * @author Boyan Stoynov
 */
public class AddTransactionActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.toolbar_addtransaction) Toolbar toolbar;
    @BindView(R.id.tablayout_addtransaction) TabLayout tabLayout;
    @BindView(R.id.spinner_addtransaction_category) Spinner categorySpinner;
    @BindView(R.id.spinner_addtransaction_account) Spinner accountSpinner;
    @BindView(R.id.dateinput_addtransaction) EditText inputDate;
    @BindView(R.id.numberinput_addtransaction_amount) EditText inputAmount;
    @BindView(R.id.spinner_addtransaction_recurringmode) Spinner recurringModeSpinner;

    CategoryDao categoryDao;
    AccountDao accountDao;
    Date date;
    boolean isRecurring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
    @OnClick(R.id.button_addtransaction_add)
    public void addTransaction() {
        // TODO need to do validation here
        createTransaction();
        Toast.makeText(getApplicationContext(), "Transaction added", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @OnClick(R.id.button_addtransaction_cancel)
    public void cancelTransaction() {
        Toast.makeText(getApplicationContext(), "Transaction discarded", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @OnClick(R.id.dateinput_addtransaction)
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

    @OnClick(R.id.checkbox_addtransaction_recurring)
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
            newTransaction.setAccount((Account) accountSpinner.getSelectedItem());
            newTransaction.setCategory((Category) categorySpinner.getSelectedItem());
            newTransaction.setAmount(new BigDecimal(inputAmount.getText().toString()));
            newTransaction.setDate(date);

            getRealmManager().createTransactionDao().saveOrUpdate(newTransaction);
        }
        else {
            Recurring newRecurring = new Recurring();
            newRecurring.setAccount((Account) accountSpinner.getSelectedItem());
            newRecurring.setCategory((Category) categorySpinner.getSelectedItem());
            newRecurring.setAmount(new BigDecimal(inputAmount.getText().toString()));
            newRecurring.setStartDate(date);

            switch (recurringModeSpinner.getSelectedItemPosition()) {
                case 0:
                    newRecurring.setMode(Recurring.Mode.MONTHLY);
                    break;
                case 1:
                    newRecurring.setMode(Recurring.Mode.BIWEEKLY);
                    break;
                case 2:
                    newRecurring.setMode(Recurring.Mode.WEEKLY);
                    break;
            }

            //TODO temporary solution below. need to implement smart next transaction calculation
            newRecurring.setNextTransaction(new Date());
            getRealmManager().createRecurringDao().saveOrUpdate(newRecurring);

            // TODO create date utils class to take care of everything related to date
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(date);
            cal2.setTime(new Date());
            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
            if (sameDay) {
                Transaction newTransaction = new Transaction();
                newTransaction.setAccount((Account) accountSpinner.getSelectedItem());
                newTransaction.setCategory((Category) categorySpinner.getSelectedItem());
                newTransaction.setAmount(new BigDecimal(inputAmount.getText().toString()));
                newTransaction.setDate(date);

                getRealmManager().createTransactionDao().saveOrUpdate(newTransaction);
            }
        }
    }
 }
