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
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Recurring;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_recurring);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                RecurringFragment recurringFragment = new RecurringFragment();
                switch (tab.getPosition()) {
                    case 0:
                        recurringFragment.setCategoryType(Category.Type.INCOME);
                        break;
                    case 1:
                        recurringFragment.setCategoryType(Category.Type.EXPENSE);
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_recurring, recurringFragment);
                transaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.getTabAt(1).select();
        tabLayout.getTabAt(0).select();

        recurringDao = getRealmManager().createRecurringDao();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_recurring;
    }

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

    @Override
    public void onEditButtonClicked(Recurring recurring) {
        //TODO need to implement recurring transaction next date calculation
        CategoryDao categoryDao = getRealmManager().createCategoryDao();
        AccountDao accountDao = getRealmManager().createAccountDao();

        EditRecurringDialog dialog = new EditRecurringDialog();
        dialog.setData(recurringDao.getUnmanaged(recurring));
        dialog.setAccountsList(accountDao.getAll());

        if (recurring.getCategory().getType() == Category.Type.INCOME)
            dialog.setCategoriesList(categoryDao.getAllIncomeCategories());
        else
            dialog.setCategoriesList(categoryDao.getAllExpenseCategories());

        dialog.show(getSupportFragmentManager(), "RECURRING_DIALOG");
    }

    @Override
    public void onDialogPositiveClick(Recurring editedRecurring) {
        Recurring recurring = recurringDao.getById(editedRecurring.getId());
        recurringDao.editAccount(recurring, editedRecurring.getAccount());
        recurringDao.editCategory(recurring, editedRecurring.getCategory());
        recurringDao.editAmount(recurring, editedRecurring.getAmount());
        recurringDao.editStartDate(recurring, editedRecurring.getStartDate());
        recurringDao.editMode(recurring, editedRecurring.getMode());
        //TODO if date changed is today , also add a new transaction also
    }
}
