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

import butterknife.BindView;

/**
 * Controller for Categories activity.
 *
 * @author Boyan Stoynov
 */
public class CategoriesActivity extends BaseActivity implements BaseRecyclerAdapter.RecyclerViewListener<Category>,BaseEditorDialog.DialogListener<Category>{

    @BindView(R.id.toolbar_categories) Toolbar toolbar;
    @BindView(R.id.tablayout_categories) TabLayout tabLayout;

    private CategoryDao categoryDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_categories);
        // TODO extract in method to reduce size of onCreate
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                CategoriesFragment categoriesFragment = new CategoriesFragment();
                switch (tab.getPosition()) {
                    case 0:
                        categoriesFragment.setCategoryType(Category.Type.INCOME);
                        break;
                    case 1:
                        categoriesFragment.setCategoryType(Category.Type.EXPENSE);
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_categories, categoriesFragment);
                transaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //TODO may want to save state of tabs here and in upper method
            }
        });
        // TODO make this get the previously selected tab from bundle and select it instead
        tabLayout.getTabAt(1).select();
        tabLayout.getTabAt(0).select();

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add:
                final Intent i = new Intent(this, AddCategoryActivity.class);
                startActivity(i);
                break;
            case android.R.id.home:
                onBackPressed();
                return true;

        }

        return true;
    }

    @Override
    public void onDeleteButtonClicked(final Category category) {
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

    @Override
    public void onEditButtonClicked(Category category) {
        CategoryDialog dialog = new CategoryDialog();
        dialog.setData(categoryDao.getUnmanaged(category));
        dialog.show(getSupportFragmentManager(), "CATEGORY_DIALOG");
    }

    @Override
    public void onDialogPositiveClick(Category editedCategory) {
        Category category = categoryDao.getById(editedCategory.getId());
        categoryDao.editName(category, editedCategory.getName());
    }
}
