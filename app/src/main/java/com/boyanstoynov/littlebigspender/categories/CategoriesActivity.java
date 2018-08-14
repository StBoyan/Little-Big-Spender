package com.boyanstoynov.littlebigspender.categories;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.BaseEditorDialog;
import com.boyanstoynov.littlebigspender.BaseRecyclerAdapter;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.dao.CategoryDao;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Transaction;

import java.util.List;

import butterknife.BindView;

import static com.boyanstoynov.littlebigspender.util.Constants.EXPENSE_POSITION;
import static com.boyanstoynov.littlebigspender.util.Constants.INCOME_POSITION;

/**
 * Controller for Categories activity. Handles creating CategoriesFragment
 * and placing it into the view, button clicks and editing/deleting categories.
 *
 * @author Boyan Stoynov
 */
public class CategoriesActivity extends BaseActivity implements
        BaseRecyclerAdapter.RecyclerViewListener<Category>,BaseEditorDialog.DialogListener<Category> {

    @BindView(R.id.toolbar_categories) Toolbar toolbar;
    @BindView(R.id.tablayout_categories) TabLayout tabLayout;

    private CategoryDao categoryDao;
    // Keys and values used in bundles in this package
    protected static final String CATEGORY_TYPE_KEY = "categoryType";
    protected static final String SELECTED_TAB_KEY = "selectedTab";
    protected static final String INCOME_TYPE_VALUE = "income";
    protected static final String EXPENSE_TYPE_VALUE = "expense";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_categories);

        setupTabSelectedListener();

        if (savedInstanceState != null)
            tabLayout.getTabAt(savedInstanceState.getInt(SELECTED_TAB_KEY)).select();
        else {// Display first tab on create
            tabLayout.getTabAt(1).select();
            tabLayout.getTabAt(0).select();
        }

        categoryDao = getRealmManager().createCategoryDao();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_categories;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addbutton_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_TAB_KEY, tabLayout.getSelectedTabPosition());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add:
                final Intent intent = new Intent(this, AddCategoryActivity.class);
                intent.putExtra(CATEGORY_TYPE_KEY, tabLayout.getSelectedTabPosition());
                startActivity(intent);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    /**
     * Handles delete button click. Checks if category has any transactions
     * associated with it. if it does shows an error message, else
     * proceeds to display a confirmation dialog.
     * @param category Category which button has been clicked
     */
    @Override
    public void onDeleteButtonClicked(Category category) {
        List<Transaction> attachedTransactions = getRealmManager().createTransactionDao().getByCategory(category);

        if (attachedTransactions.size() > 0)
            deleteErrorDialog();
        else
            deleteConfirmationDialog(category);
    }

    /**
     * Shows the edit dialog when the edit button is clicked.
     * @param category Category whose button has been clicked
     */
    @Override
    public void onEditButtonClicked(Category category) {
        EditCategoryDialog dialog = new EditCategoryDialog();
        dialog.setData(categoryDao.getUnmanaged(category));
        dialog.show(getSupportFragmentManager(), "CATEGORY_DIALOG");
    }

    /**
     * Handles saving the edited name of a category to the database.
     * Callback from edit dialog.
     * @param editedCategory object representing the new state of the
     *                       category
     */
    @Override
    public void onDialogPositiveClick(Category editedCategory) {
        Category category = categoryDao.getById(editedCategory.getId());
        categoryDao.editName(category, editedCategory.getName());
    }

    /**
     * Informs the user a category cannot be deleted.
     */
    private void deleteErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.all_error);
        builder.setMessage(R.string.categories_cannot_delete_error);
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
     * Shows the user a dialog asking to confirm a category deletion.
     * @param category Category to be deleted
     */
    private void deleteConfirmationDialog(final Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(String.format("%s %s?", getResources().getString(R.string.all_warning_delete_message), category.getName()));
        builder.setIcon(R.drawable.ic_warning);
        builder.setPositiveButton(R.string.all_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                categoryDao.delete(category);
                Toast.makeText(getBaseContext(), R.string.categories_delete_toast, Toast.LENGTH_SHORT).show();
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
     * Sets up the onTabSelectedListener for the tab layout.
     */
    private void setupTabSelectedListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                CategoriesFragment categoriesFragment = new CategoriesFragment();
                Bundle fragmentBundle = new Bundle();
                categoriesFragment.setArguments(fragmentBundle);
                switch (tab.getPosition()) {
                    case INCOME_POSITION:
                        fragmentBundle.putString(CATEGORY_TYPE_KEY, INCOME_TYPE_VALUE);
                        break;
                    case EXPENSE_POSITION:
                        fragmentBundle.putString(CATEGORY_TYPE_KEY, EXPENSE_TYPE_VALUE);
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_categories, categoriesFragment);
                transaction.commit();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}
