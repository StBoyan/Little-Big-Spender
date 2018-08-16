package com.boyanstoynov.littlebigspender.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.boyanstoynov.littlebigspender.BaseEditorDialog;
import com.boyanstoynov.littlebigspender.BaseRecyclerAdapter;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.api.CryptoClient;
import com.boyanstoynov.littlebigspender.db.dao.AccountDao;
import com.boyanstoynov.littlebigspender.db.dao.CategoryDao;
import com.boyanstoynov.littlebigspender.db.dao.TransactionDao;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Transaction;
import com.boyanstoynov.littlebigspender.main.accounts.AccountsAdapter;
import com.boyanstoynov.littlebigspender.main.accounts.AddCryptoActivity;
import com.boyanstoynov.littlebigspender.main.accounts.EditAccountDialog;
import com.boyanstoynov.littlebigspender.main.accounts.AccountsFragment;
import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.main.accounts.AddAccountActivity;
import com.boyanstoynov.littlebigspender.main.accounts.TransferDialog;
import com.boyanstoynov.littlebigspender.main.overview.OverviewFragment;
import com.boyanstoynov.littlebigspender.main.transactions.EditTransactionDialog;
import com.boyanstoynov.littlebigspender.main.transactions.FilterDialog;
import com.boyanstoynov.littlebigspender.main.transactions.TransactionsFragment;

import com.boyanstoynov.littlebigspender.about.AboutActivity;
import butterknife.BindView;
import io.realm.RealmObject;

import com.boyanstoynov.littlebigspender.categories.CategoriesActivity;
import com.boyanstoynov.littlebigspender.intro.IntroActivity;
import com.boyanstoynov.littlebigspender.recurring.RecurringActivity;
import com.boyanstoynov.littlebigspender.settings.SettingsActivity;
import com.boyanstoynov.littlebigspender.statistics.StatisticsActivity;
import com.boyanstoynov.littlebigspender.util.DateTimeUtils;
import com.boyanstoynov.littlebigspender.util.InitialSetupRunnable;
import com.boyanstoynov.littlebigspender.util.SharedPrefsManager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Controller for the main activity which is displayed upon
 * pressing the application icon. If this is the first launch, shows
 * the introduction intro first and does initial setup. Responsible for
 * listening and responding to events from all the fragments attached to
 * the main activity (overview, accounts, transactions) and changing the
 * current fragment as appropriate. Also, shows a side navigation drawer
 * and starts the appropriate activity as requested by the user.
 *
 * @author Boyan Stoynov
 */
public class MainActivity extends BaseActivity
        implements BaseRecyclerAdapter.RecyclerViewListener<RealmObject>,
        BaseEditorDialog.DialogListener<RealmObject>, AccountsAdapter.CryptoRefreshButtonListener,
        CryptoClient.ClientCallback{

    @BindView(R.id.drawer) DrawerLayout drawer;
    @BindView(R.id.toolbar_main) Toolbar toolbar;
    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.drawer_navigation) NavigationView navigationView;

    private TransactionDao transactionDao;
    private AccountDao accountDao;
    private CategoryDao categoryDao;
    private Account currentlyUpdatingCryptoAccount;
    private CryptoClient cryptoClient;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* If application is launched for the first time start Intro
        activity and execute initial setup on background thread */
        if (SharedPrefsManager.read(getResources().getString(R.string.firstStart),true)) {
            startActivity(new Intent(this, IntroActivity.class));
            AsyncTask.execute(new InitialSetupRunnable(this));
            SharedPrefsManager.write(getResources().getString(R.string.firstStart), false);
        }

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer_menu);

        transactionDao = getRealmManager().createTransactionDao();
        accountDao = getRealmManager().createAccountDao();
        categoryDao = getRealmManager().createCategoryDao();
        cryptoClient = new CryptoClient(this);
        fragmentManager = getSupportFragmentManager();

        setUpBottomNavigationItemSelectedListener();
        setUpNavigationDrawerListener();

        //Navigates to overview screen upon first launch
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_main, new OverviewFragment());
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(Gravity.START);
                return true;
            case R.id.item_add_account:
                final Intent addAccountIntent = new Intent(this, AddAccountActivity.class);
                startActivity(addAccountIntent);
                return true;
            case R.id.item_add_crypto:
                final Intent addCryptoIntent = new Intent(this, AddCryptoActivity.class);
                startActivity(addCryptoIntent);
                return true;
            case R.id.item_filter:
                showFilterDialog();
                return true;
            case R.id.item_transfer_account:
                TransferDialog transferDialog = new TransferDialog();
                transferDialog.show(getFragmentManager(), "TRANSFER_DIALOG");
                transferDialog.setAccountList(getRealmManager().createAccountDao().getAllFiat());
                transferDialog.setCallback(new TransferDialog.TransferDialogCallback() {
                    @Override
                    public void onTransferAccount(Account from, Account to, BigDecimal amount) {
                        accountDao.subtractAmount(from, amount);
                        accountDao.addAmount(to, amount);
                    }
                });
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Shows the filter dialog to the user.
     */
    private void showFilterDialog() {
        FilterDialog filterDialog = new FilterDialog();

        filterDialog.show(getFragmentManager(), "FILTER_DIALOG");
        filterDialog.setCategoryList(categoryDao.getAll());
        filterDialog.setAccountList(accountDao.getAllFiat());
        filterDialog.setCallback(new FilterDialog.FilterSelectedCallback() {
            TransactionsFragment fragment = (TransactionsFragment) getSupportFragmentManager().findFragmentById(R.id.frame_main);
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
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    /**
     * Handles a delete button click on either the account or
     * transaction fragment and shows the appropriate dialog.
     * For account deletions, check if the account is associated
     * with any transactions first and displays an error message
     * if it is.
     * @param item item to be deleted
     */
    @Override
    public void onDeleteButtonClicked(RealmObject item) {
        if (item instanceof Account) {
            if (accountHasTransactions((Account) item))
                showAccountDeletionError();
            else
                showDeleteAccountDialog((Account) item);
        }
        else if (item instanceof Transaction)
            showDeleteTransactionDialog((Transaction) item);
    }

    /**
     * Returns boolean whether account has any transactions
     * associated with it.
     * @param account Account object
     * @return boolean whether account has transactions
     */
    private boolean accountHasTransactions(Account account) {
        List<Transaction> transactions = transactionDao.getByAccount(account);
        Log.d("transactions size", String.valueOf(transactions.size()));
        return transactions.size() != 0;
    }

    /**
     * Shows an error message to user informing that account
     * cannot be deleted.
     */
    private void showAccountDeletionError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.all_error);
        builder.setMessage(R.string.accounts_cannot_delete_error);
        builder.setPositiveButton(R.string.all_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Handles an edit button click on either the account
     * or transaction fragment and shows the appropriate
     * edit dialog.
     * @param item item to be edited
     */
    @Override
    public void onEditButtonClicked(RealmObject item) {
        if (item instanceof Account)
            showEditAccountDialog((Account) item);
        else if (item instanceof Transaction)
            showEditTransactionDialog((Transaction) item);
    }

    /**
     * Handles a positive button click on the edit dialog
     * for either a transaction or an account and calls the
     * appropriate edit method.
     * @param item edited item
     */
    @Override
    public void onDialogPositiveClick(RealmObject item) {
        if (item instanceof Account)
            editAccount((Account) item);
          else if (item instanceof Transaction)
            editTransaction((Transaction) item);

    }

    /**
     * Displays the confirmation dialog before deleting an account. If
     * the user confirms, deletes the account and displays a toast to
     * inform the user.
     * @param account Account to be deleted
     */
    public void showDeleteAccountDialog(final Account account) {
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

    /**
     * Displays the confirmation dialog before deleting a transaction. If
     * the user confirms, deletes the transaction and displays a toast to
     * inform the user.
     * @param transaction Transaction to be deleted
     */
    public void showDeleteTransactionDialog(final Transaction transaction) {
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

    /**
     * Shows the edit account dialog and pass it an unmanaged
     * object of the account to be edited.
     * @param account Account to be edited
     */
    public void showEditAccountDialog(Account account) {
        AccountDao accountDao = getRealmManager().createAccountDao();
        EditAccountDialog dialog = new EditAccountDialog();
        dialog.setData(accountDao.getUnmanaged(account));
        dialog.show(getSupportFragmentManager(), "ACCOUNT_DIALOG");
    }

    /**
     * Shows the edit transaction dialog and pass it an unmanaged
     * object of the transaction to be edited.
     * @param transaction Transaction to be edited
     */
    public void showEditTransactionDialog(Transaction transaction) {
        EditTransactionDialog dialog = new EditTransactionDialog();
        dialog.setData(transactionDao.getUnmanaged(transaction));
        if (transaction.getAccount().isCrypto())
            dialog.setAccountsList(accountDao.getAll());
        else
            dialog.setAccountsList(accountDao.getAllFiat());

        if (transaction.getCategory().getType() == Category.Type.INCOME)
            dialog.setCategoriesList(categoryDao.getAllIncomeCategories());
        else
            dialog.setCategoriesList(categoryDao.getAllExpenseCategories());

        dialog.show(getSupportFragmentManager(), "TRANSACTION_DIALOG");
    }

    /**
     * Modifies the account and saves its values to the database.
     * @param editedAccount unmanaged Account object
     */
    public void editAccount(Account editedAccount) {
        Account account = accountDao.getById(editedAccount.getId());
        accountDao.editName(account, editedAccount.getName());
        accountDao.editBalance(account, editedAccount.getBalance());
    }

    /**
     * Modifies the transaction and saves its values to the database.
     * Also, if account is changed old account is refunded or debited
     * the funds of the transaction and the new one is is charged or
     * credited appropriately.
     * @param editedTransaction unmanaged Transaction object
     */
    public void editTransaction(Transaction editedTransaction) {
        Transaction transaction = transactionDao.getById(editedTransaction.getId());

        //Subtract income from old account and add it to new one
        if (transaction.getCategory().getType() == Category.Type.INCOME) {
            accountDao.subtractAmount(transaction.getAccount(), transaction.getAmount());
            accountDao.addAmount(
                    accountDao.getById(editedTransaction.getAccount().getId()), editedTransaction.getAmount());
        }
        else {//Refund expense to old account and take it from new one
            accountDao.addAmount(transaction.getAccount(), transaction.getAmount());
            accountDao.subtractAmount(
                    accountDao.getById(editedTransaction.getAccount().getId()), editedTransaction.getAmount());
        }

        transactionDao.editAccount(transaction, editedTransaction.getAccount());
        transactionDao.editCategory(transaction, editedTransaction.getCategory());
        transactionDao.editAmount(transaction, editedTransaction.getAmount());
        transactionDao.editDate(transaction, editedTransaction.getDate());
    }

    /**
     * Handles a click to the refresh button of the accounts fragment. This method
     * is synchronized so that several consecutive calls to it will not interfere
     * with the currently updated amount. Displays a toast notifying the user that
     * the refresh is underway and tries to fetch the most recent exchange rate
     * for the current home currency.
     * @param cryptoAccountId ID field of the cryptocurrency account to be fetched
     */
    @Override
    public synchronized void onRefreshButtonClicked(final String cryptoAccountId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentlyUpdatingCryptoAccount = accountDao.getById(cryptoAccountId);
                String toastText = String.format("%s %s",
                        getResources().getString(R.string.accounts_refreshAttempt), currentlyUpdatingCryptoAccount.toString());

                Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();

                String[] cryptoNames = getResources().getStringArray(R.array.crypto_names);
                String[] cryptoCodes = getResources().getStringArray(R.array.crypto_codes);

                //Gets the cryptocurrency's code used by the API
                for (int i = 0; i < cryptoNames.length; i++) {
                    if (currentlyUpdatingCryptoAccount.getName().equals(cryptoNames[i])) {
                        cryptoClient.fetchPrice(cryptoCodes[i], SharedPrefsManager.getCurrencyCode());
                        break;
                    }
                }

            }
        });

    }

    /**
     * Informs user of unsuccessful refresh of cryptocurrency account's
     * fiat value.
     */
    @Override
    public void onFetchUnsuccessful() {
        Toast.makeText(this, R.string.accounts_refreshFailed, Toast.LENGTH_SHORT).show();
    }

    /**
     * Saves the fetched fiat value for the cryptocurrency and sets its
     * last updated to the present time. Also, informs the user of success.
     * @param fiatValue new fiat value of cryptocurrency
     */
    @Override
    public void onFetchSuccessful(final BigDecimal fiatValue) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, R.string.accounts_refreshCompleted, Toast.LENGTH_SHORT).show();
                accountDao.editFiatValue(currentlyUpdatingCryptoAccount, fiatValue);
                accountDao.editLastUpdated(currentlyUpdatingCryptoAccount, DateTimeUtils.getCurrentTimeInMillis());
            }
        });
      }

    /**
     * Set up the listener for selections of items on the side
     * navigation drawer, responsible for starting their activities
     * upon user selection.
     */
    private void setUpNavigationDrawerListener() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        drawer.closeDrawers();
                        Intent intent = null;

                        switch (item.getItemId()) {
                            case R.id.item_settings:
                                intent = new Intent(MainActivity.this, SettingsActivity.class);
                                break;
                            case R.id.item_categories:
                                intent = new Intent(MainActivity.this, CategoriesActivity.class);
                                break;
                            case R.id.item_recurring:
                                intent = new Intent(MainActivity.this, RecurringActivity.class);
                                break;
                            case R.id.item_statistics:
                                intent = new Intent(MainActivity.this, StatisticsActivity.class);
                                break;
                            case R.id.item_about:
                                intent = new Intent(MainActivity.this, AboutActivity.class);
                                break;
                        }
                        startActivity(intent);
                        return true;
                    }
                }
        );
    }

    /**
     * Set up the listener for selections on the bottom navigation
     * bar that is responsible for changing the currently displayed
     * fragment.
     */
    private void setUpBottomNavigationItemSelectedListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(true);
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.item_overview:
                                selectedFragment = new OverviewFragment();
                                break;
                            case R.id.item_transactions:
                                selectedFragment = new TransactionsFragment();
                                break;
                            case R.id.item_accounts:
                                selectedFragment = new AccountsFragment();
                                break;
                        }
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.frame_main, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                }
        );
    }
}
