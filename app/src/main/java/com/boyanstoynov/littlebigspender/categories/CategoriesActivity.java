package com.boyanstoynov.littlebigspender.categories;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Category;

import butterknife.BindView;

/**
 * Controller for Categories activity.
 *
 * @author Boyan Stoynov
 */
public class CategoriesActivity extends BaseActivity {

    @BindView(R.id.toolbar_categories) Toolbar toolbar;
    @BindView(R.id.tablayout_categories) TabLayout tabLayout;

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

    // TODO need to refactor if no other items will be in toolbar (i.e. remove switch)
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
}
