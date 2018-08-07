package com.boyanstoynov.littlebigspender.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.boyanstoynov.littlebigspender.BaseEditorDialog;
import com.boyanstoynov.littlebigspender.BaseRecyclerAdapter;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.dao.AccountDao;
import com.boyanstoynov.littlebigspender.db.dao.CategoryDao;
import com.boyanstoynov.littlebigspender.db.dao.TransactionDao;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Transaction;
import com.boyanstoynov.littlebigspender.main.accounts.AccountDialog;
import com.boyanstoynov.littlebigspender.main.accounts.AccountsFragment;
import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.main.accounts.AddAccountActivity;
import com.boyanstoynov.littlebigspender.main.accounts.TransferDialog;
import com.boyanstoynov.littlebigspender.main.overview.OverviewFragment;
import com.boyanstoynov.littlebigspender.main.transactions.FilterDialog;
import com.boyanstoynov.littlebigspender.main.transactions.TransactionDialog;
import com.boyanstoynov.littlebigspender.main.transactions.TransactionsFragment;

import com.boyanstoynov.littlebigspender.about.AboutActivity;
import butterknife.BindView;
import io.realm.RealmObject;

import com.boyanstoynov.littlebigspender.categories.CategoriesActivity;
import com.boyanstoynov.littlebigspender.intro.IntroActivity;
import com.boyanstoynov.littlebigspender.recurring.RecurringActivity;
import com.boyanstoynov.littlebigspender.settings.SettingsActivity;
import com.boyanstoynov.littlebigspender.statistics.StatisticsActivity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Controller for main screen activity.
 *
 * @author Boyan Stoynov
 */
public class MainActivity extends BaseActivity implements BaseRecyclerAdapter.RecyclerViewListener<RealmObject>, BaseEditorDialog.DialogListener<RealmObject> {

    @BindView(R.id.drawer) DrawerLayout drawer;
    @BindView(R.id.toolbar_main) Toolbar toolbar;
    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.drawer_navigation) NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO use shared preference class
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!preferences.getBoolean("firstStart", false)) {

            startIntroOnFirstLaunch();

            firstLaunchSetUp();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstStart", true);
            editor.apply();
        }


        //TODO consider making toolbar part of a superclass to use in other activities
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer_menu);


        //TODO consider putting listener methods separately to reduce size of onCreate
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(true);
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.item_add:
                                selectedFragment = new OverviewFragment();
                                break;
                            case R.id.item_transactions:
                                selectedFragment = new TransactionsFragment();
                                break;
                            case R.id.item_accounts:
                                selectedFragment = new AccountsFragment();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_main, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                }
        );

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        drawer.closeDrawers();
                        Intent i = null;

                        switch (item.getItemId()) {
                            case R.id.item_settings:
                                i = new Intent(MainActivity.this, SettingsActivity.class);
                                break;
                            case R.id.item_categories:
                                i = new Intent(MainActivity.this, CategoriesActivity.class);
                                break;
                            case R.id.item_recurring:
                                i = new Intent(MainActivity.this, RecurringActivity.class);
                                break;
                            case R.id.item_statistics:
                                i = new Intent(MainActivity.this, StatisticsActivity.class);
                                break;
                            case R.id.item_about:
                                i = new Intent(MainActivity.this, AboutActivity.class);
                                break;
                        }

                        startActivity(i);
                        return true;
                    }
                }
        );
    }

    private void firstLaunchSetUp() {
        AccountDao accountDao = getRealmManager().createAccountDao();

        Account acc1 = new Account();
        acc1.setName("Bank Account");
        acc1.setBalance(new BigDecimal(0));

        Account acc2 = new Account();
        acc2.setName("Cash");
        acc2.setBalance(new BigDecimal(0));

        accountDao.save(acc1);
        accountDao.save(acc2);

        CategoryDao categoryDao = getRealmManager().createCategoryDao();

        Category cat1 = new Category();
        cat1.setName("Household");
        cat1.setType(Category.Type.EXPENSE);

        Category cat2 = new Category();
        cat2.setName("Entertainment");
        cat2.setType(Category.Type.EXPENSE);

        Category cat3 = new Category();
        cat3.setName("Utilities");
        cat3.setType(Category.Type.EXPENSE);

        Category cat4 = new Category();
        cat4.setName("Rent");
        cat4.setType(Category.Type.EXPENSE);

        Category cat5 = new Category();
        cat5.setName("Misc");
        cat5.setType(Category.Type.EXPENSE);

        Category cat6 = new Category();
        cat6.setName("Clothing");
        cat6.setType(Category.Type.EXPENSE);

        Category cat7 = new Category();
        cat7.setName("Salary");
        cat7.setType(Category.Type.INCOME);

        Category cat8 = new Category();
        cat8.setName("Interest");
        cat8.setType(Category.Type.INCOME);

        Category cat9 = new Category();
        cat9.setName("Dividends");
        cat9.setType(Category.Type.INCOME);

        Category cat10 = new Category();
        cat10.setName("Other");
        cat10.setType(Category.Type.INCOME);

        categoryDao.save(cat1);
        categoryDao.save(cat2);
        categoryDao.save(cat3);
        categoryDao.save(cat4);
        categoryDao.save(cat5);
        categoryDao.save(cat6);
        categoryDao.save(cat7);
        categoryDao.save(cat8);
        categoryDao.save(cat9);
        categoryDao.save(cat10);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(Gravity.START);
                return true;
            case R.id.item_add_account:
                final Intent intent = new Intent(this, AddAccountActivity.class);
                startActivity(intent);
                return true;
            case R.id.item_filter:
                FilterDialog filterDialog = new FilterDialog();
                //TODO extract in a method

                filterDialog.show(getFragmentManager(), "FILTER_DIALOG");
                filterDialog.setCategoryList(getRealmManager().createCategoryDao().getAll());
                filterDialog.setAccountList(getRealmManager().createAccountDao().getAll());
                filterDialog.setCallback(new FilterDialog.FilterSelectedCallback() {
                    TransactionsFragment fragment = (TransactionsFragment) getSupportFragmentManager().findFragmentById(R.id.frame_main);
                    TransactionDao transactionDao = getRealmManager().createTransactionDao();
                    @Override
                    public void onTypeFilterSelected(Category.Type type) {
                        fragment.populateRecyclerView(transactionDao.getByType(type));
                    }

                    @Override
                    public void onCategoryFilterSelected(Category category) {
                        fragment.populateRecyclerView(transactionDao.getByCategory(category));
                    }

                    @Override
                    public void onAccountFilterSelected(Account account) {
                        fragment.populateRecyclerView(transactionDao.getByAccount(account));
                    }

                    @Override
                    public void onDateFilterSelected(Date date) {
                        fragment.populateRecyclerView(transactionDao.getByDate(date));
                    }

                    @Override
                    public void onResetSelected() {
                        fragment.populateRecyclerView(transactionDao.getAll());
                    }
                });
                return true;
            case R.id.item_transfer_account:
                TransferDialog transferDialog = new TransferDialog();
                transferDialog.show(getFragmentManager(), "TRANSFER_DIALOG");
                transferDialog.setAccountList(getRealmManager().createAccountDao().getAll());
                transferDialog.setCallback(new TransferDialog.TransferDialogCallback() {
                    @Override
                    public void onTransferAccount(Account from, Account to, BigDecimal amount) {
                        AccountDao accountDao = getRealmManager().createAccountDao();
                        accountDao.subtractAmount(from, amount);
                        accountDao.addAmount(to, amount);
                    }
                });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    /**
     * Launch Intro activity upon first launch.
     */
    public void startIntroOnFirstLaunch() {
        // TODO separate first start settings in a separate util class
        // TODO ALSO could initialise currency to locale there too
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDeleteButtonClicked(RealmObject item) {
        if (item instanceof Account)
            showDeleteAccountDialog((Account) item);
        else if (item instanceof Transaction)
            showDeleteTransactionDialog((Transaction) item);
    }

    @Override
    public void onEditButtonClicked(RealmObject item) {
        if (item instanceof Account)
            showEditAccountDialog((Account) item);
        else if (item instanceof Transaction)
            showEditTransactionDialog((Transaction) item);
    }

    @Override
    public void onDialogPositiveClick(RealmObject item) {
        if (item instanceof Account)
            editAccount((Account) item);
        else if (item instanceof Transaction)
            editTransaction((Transaction) item);

    }
    // TODO initialise Account and Transaction Dao in onCreate and store them
    public void showDeleteAccountDialog(final Account account) {
        final AccountDao accountDao = getRealmManager().createAccountDao();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(String.format("%s %s?", getResources().getString(R.string.all_warning_delete_message), account.getName()));
        builder.setIcon(R.drawable.ic_warning);
        builder.setPositiveButton(R.string.all_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                accountDao.delete(account);
                Toast.makeText(getBaseContext(), R.string.accounts_delete_toast, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.all_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showDeleteTransactionDialog(final Transaction transaction) {
        final TransactionDao transactionDao = getRealmManager().createTransactionDao();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.transaction_warning_delete_message);
        builder.setIcon(R.drawable.ic_warning);
        builder.setPositiveButton(R.string.all_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                transactionDao.delete(transaction);
                Toast.makeText(getBaseContext(), R.string.transaction_delete_toast, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.all_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showEditAccountDialog(Account account) {
        AccountDao accountDao = getRealmManager().createAccountDao();
        AccountDialog dialog = new AccountDialog();
        dialog.setData(accountDao.getUnmanaged(account));
        dialog.show(getSupportFragmentManager(), "ACCOUNT_DIALOG");
    }

    public void showEditTransactionDialog(Transaction transaction) {
        TransactionDao transactionDao = getRealmManager().createTransactionDao();
        CategoryDao categoryDao = getRealmManager().createCategoryDao();
        AccountDao accountDao = getRealmManager().createAccountDao();

        TransactionDialog dialog = new TransactionDialog();
        dialog.setData(transactionDao.getUnmanaged(transaction));
        dialog.setAccountsList(accountDao.getAll());

        if (transaction.getCategory().getType() == Category.Type.INCOME)
            dialog.setCategoriesList(categoryDao.getAllIncomeCategories());
        else
            dialog.setCategoriesList(categoryDao.getAllExpenseCategories());

        dialog.show(getSupportFragmentManager(), "TRANSACTION_DIALOG");
    }

    public void editAccount(Account editedAccount) {
        AccountDao accountDao = getRealmManager().createAccountDao();
        Account account = accountDao.getById(editedAccount.getId());
        accountDao.editName(account, editedAccount.getName());
        accountDao.editBalance(account, editedAccount.getBalance());
    }

    public void editTransaction(Transaction editedTransaction) {
        TransactionDao transactionDao = getRealmManager().createTransactionDao();
        Transaction transaction = transactionDao.getById(editedTransaction.getId());
        transactionDao.editAccount(transaction, editedTransaction.getAccount());
        transactionDao.editCategory(transaction, editedTransaction.getCategory());
        transactionDao.editAmount(transaction, editedTransaction.getAmount());
        transactionDao.editDate(transaction, editedTransaction.getDate());
    }
}
