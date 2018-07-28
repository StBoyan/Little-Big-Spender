package com.boyanstoynov.littlebigspender.main.transactions;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.db.dao.AccountDao;
import com.boyanstoynov.littlebigspender.db.dao.CategoryDao;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Transaction;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.RealmResults;

/**
 * Controller for Add transaction activity.
 *
 * @author Boyan Stoynov
 */
public class AddTransactionActivity extends BaseActivity {

    @BindView(R.id.toolbar_addtransaction) Toolbar toolbar;
    @BindView(R.id.tablayout_addtransaction) TabLayout tabLayout;
    @BindView(R.id.spinner_addtransaction_category) Spinner categorySpinner;
    @BindView(R.id.spinner_addtransaction_account) Spinner accountSpinner;
    @BindView(R.id.dateinput_addtransaction) EditText inputDate;
    @BindView(R.id.numberinput_addtransaction_amount) EditText inputAmount;

    CategoryDao categoryDao;
    AccountDao accountDao;

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

        SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");

        inputDate.setText(df.format(new Date()));
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

    public void createTransaction() {
        Transaction newTransaction = new Transaction();
        newTransaction.setAccount((Account)accountSpinner.getSelectedItem());
        newTransaction.setCategory((Category)categorySpinner.getSelectedItem());
        newTransaction.setAmount(new BigDecimal(inputAmount.getText().toString()));
        newTransaction.setDate(new Date());
        getRealmManager().createTransactionDao().saveOrUpdate(newTransaction);
    }
}
