package com.boyanstoynov.littlebigspender.categories;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.boyanstoynov.littlebigspender.BaseFragment;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.RealmManager;
import com.boyanstoynov.littlebigspender.db.dao.CategoryDao;
import com.boyanstoynov.littlebigspender.db.model.Category;

import java.util.List;

import butterknife.BindView;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Controller for Categories fragment.
 *
 * @author Boyan Stoynov
 */
public class CategoriesFragment extends BaseFragment {

    @BindView(R.id.recyclerview_categories) RecyclerView recyclerView;

    private CategoriesAdapter adapter;
    private RealmResults<Category> categoriesRealmResults;
    private CategoryDao categoryDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        initViews();
        loadCategoryList();

        return view;
    }

    /**
     * @inheritDoc
     */
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_categories;
    }

    protected void setCategoryType(Category.Type type) {
        //TODO figure out how to initialise categoryDao
        RealmManager rm = new RealmManager();
        rm.open();
        categoryDao = rm.createCategoryDao();

        if (type == Category.Type.INCOME)
            categoriesRealmResults = categoryDao.getAllIncomeCategories();
        else
            categoriesRealmResults = categoryDao.getAllExpenseCategories();
    }

    private void initViews() {
        adapter = new CategoriesAdapter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void loadCategoryList() {
        if (categoriesRealmResults == null)
            throw new IllegalStateException("Categories type unspecified. Call setCategoryType() first.");

        categoriesRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Category>>() {
            @Override
            public void onChange(RealmResults<Category> categories) {
                populateRecyclerView(categories);
            }
        });

        populateRecyclerView(categoriesRealmResults);
    }

    private void populateRecyclerView(List<Category> categoriesList) {
        if (adapter != null && categoriesList != null) {
            adapter.setData(categoriesList);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Creates an AlertDialog upon delete button clicked
     * to confirm deletion. Delete if yes is clicked.
     * @param categoryName name of category to be deleted
     */
    public void onDeleteButtonClicked(final String categoryName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.app_name);
        builder.setMessage(String.format("%s %s?", getResources().getString(R.string.all_warning_delete_message), categoryName));
        builder.setIcon(R.drawable.ic_warning);
        builder.setPositiveButton(R.string.all_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                categoryDao.deleteByName(categoryName);
                Toast.makeText(getContext(), R.string.categories_delete_toast, Toast.LENGTH_SHORT).show();
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

}
