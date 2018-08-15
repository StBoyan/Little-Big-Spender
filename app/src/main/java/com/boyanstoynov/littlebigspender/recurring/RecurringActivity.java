package com.boyanstoynov.littlebigspender.recurring;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.BaseEditorDialog;
import com.boyanstoynov.littlebigspender.BaseRecyclerAdapter;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.dao.AccountDao;
import com.boyanstoynov.littlebigspender.db.dao.CategoryDao;
import com.boyanstoynov.littlebigspender.db.dao.RecurringDao;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Recurring;
import com.boyanstoynov.littlebigspender.util.DateTimeUtils;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;

/**
 * Controller for recurring transactions activity.
 *
 * @author Boyan Stoynov
 */
public class RecurringActivity extends BaseActivity implements BaseRecyclerAdapter.RecyclerViewListener<Recurring>, BaseEditorDialog.DialogListener<Recurring> {

    @BindView(R.id.toolbar_recurring) Toolbar toolbar;
    @BindView(R.id.tablayout_recurring) TabLayout tabLayout;

    private RecurringDao recurringDao;

    // Key and values used in bundles in this package
    protected static final String SELECTED_TAB_KEY = "selectedTab";
    protected static final String CATEGORY_TYPE_KEY = "categoryType";
    protected static final String INCOME_TYPE_VALUE = "income";
    protected static final String EXPENSE_TYPE_VALUE = "expense";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_recurring);

        setupTabSelectedListener();

        // On configuration change, get previously selected tab
        if (savedInstanceState != null)
            tabLayout.getTabAt(savedInstanceState.getInt(SELECTED_TAB_KEY)).select();
        else {// Display first tab on create
            tabLayout.getTabAt(1).select();
            tabLayout.getTabAt(0).select();
        }

        recurringDao = getRealmManager().createRecurringDao();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_TAB_KEY, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_recurring;
    }

    /**
     * Handles delete button click. Displays a dialog to the user prompting
     * for confirmation. Deletes the recurring transaction upon confirmation.
     * @param recurring recurring transaction to be deleted
     */
    @Override
    public void onDeleteButtonClicked(final Recurring recurring) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.recurring_warning_delete_message);
        builder.setIcon(R.drawable.ic_warning);
        builder.setPositiveButton(R.string.all_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                recurringDao.delete(recurring);
                Toast.makeText(getBaseContext(), R.string.recurring_delete_toast, Toast.LENGTH_SHORT).show();
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
     * Shows the edit dialog when the edit button is clicked.
     * @param recurring Category whose button has been clicked
     */
    @Override
    public void onEditButtonClicked(Recurring recurring) {
        CategoryDao categoryDao = getRealmManager().createCategoryDao();
        AccountDao accountDao = getRealmManager().createAccountDao();

        EditRecurringDialog dialog = new EditRecurringDialog();
        dialog.setData(recurringDao.getUnmanaged(recurring));
        /* If recurring transaction is associated with cryptocurrency account
        pass a list only with that account since its not changing account for
         cryptocurrency is not allowed */
        if (recurring.getAccount().isCrypto()) {
            ArrayList<Account> cryptoAccount = new ArrayList<>();
            cryptoAccount.add(recurring.getAccount());
            dialog.setAccountsList(cryptoAccount);
        }
        // Else pass only fiat accounts list
        else
            dialog.setAccountsList(accountDao.getAllFiat());

        if (recurring.getCategory().getType() == Category.Type.INCOME)
            dialog.setCategoriesList(categoryDao.getAllIncomeCategories());
        else
            dialog.setCategoriesList(categoryDao.getAllExpenseCategories());

        dialog.show(getSupportFragmentManager(), "RECURRING_DIALOG");
    }

    /**
     * Saves the edited fields of the Recurring transaction to the
     * database. Also calculates the new next transaction date.
     * @param editedRecurring object representing the new state of the
     *                        recurring transaction
     */
    @Override
    public void onDialogPositiveClick(Recurring editedRecurring) {
        Recurring recurring = recurringDao.getById(editedRecurring.getId());
        recurringDao.editAccount(recurring, editedRecurring.getAccount());
        recurringDao.editCategory(recurring, editedRecurring.getCategory());
        recurringDao.editAmount(recurring, editedRecurring.getAmount());
        Date startDate = editedRecurring.getStartDate();
        recurringDao.editStartDate(recurring, startDate);
        Recurring.Mode mode = editedRecurring.getMode();
        recurringDao.editMode(recurring, mode);
        //Edit next transaction date based on the new mode
        Date nextTransactionDate = startDate;
        switch (mode) {
            case MONTHLY:
                while (!DateTimeUtils.dateHasPassed(nextTransactionDate)) {
                    nextTransactionDate = DateTimeUtils.addMonth(nextTransactionDate);
                }
                break;
            case BIWEEKLY:
                while (!DateTimeUtils.dateHasPassed(nextTransactionDate)) {
                    nextTransactionDate = DateTimeUtils.addTwoWeeks(nextTransactionDate);
                }
                break;
            case WEEKLY:
                while (!DateTimeUtils.dateHasPassed(nextTransactionDate)) {
                    nextTransactionDate = DateTimeUtils.addWeek(nextTransactionDate);
                }
                break;
        }
        recurringDao.editNextTransactionDate(recurring, nextTransactionDate);
    }

    private void setupTabSelectedListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                RecurringFragment recurringFragment = new RecurringFragment();
                Bundle fragmentBundle = new Bundle();
                recurringFragment.setArguments(fragmentBundle);
                switch (tab.getPosition()) {
                    case 0:
                        fragmentBundle.putString(CATEGORY_TYPE_KEY, INCOME_TYPE_VALUE);
                        break;
                    case 1:
                        fragmentBundle.putString(CATEGORY_TYPE_KEY, EXPENSE_TYPE_VALUE);
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_recurring, recurringFragment);
                transaction.commit();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}
