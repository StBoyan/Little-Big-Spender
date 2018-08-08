package com.boyanstoynov.littlebigspender.categories;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boyanstoynov.littlebigspender.BaseFragment;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.dao.RealmManager;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        initViews();
        loadCategoryList();

        return view;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_categories;
    }

    protected void setCategoryType(Category.Type type) {
        //TODO figure out how to initialise categoryDao - possible solution is to move initialisation in onCreate in BaseFragment
        RealmManager rm = new RealmManager();
        rm.open();
        CategoryDao categoryDao = rm.createCategoryDao();

        if (type == Category.Type.INCOME)
            categoriesRealmResults = categoryDao.getAllIncomeCategories();
        else
            categoriesRealmResults = categoryDao.getAllExpenseCategories();
    }

    private void initViews() {
        adapter = new CategoriesAdapter((CategoriesActivity)getActivity());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void loadCategoryList() {
        if (categoriesRealmResults == null)
            throw new IllegalStateException("Categories type unspecified. Call setCategoryType() first.");
        //TODO IMPORTANT !!!! UNREGISTER CHANGE LISTENERS ONDESTROY SEE REALM DOC FOR MORE INFO
        categoriesRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Category>>() {
            @Override
            public void onChange(@NonNull RealmResults<Category> categories) {
                populateRecyclerView(categories);
            }
        });

        populateRecyclerView(categoriesRealmResults);
    }

    private void populateRecyclerView(List<Category> categoriesList) {
        adapter.setData(categoriesList);
    }
}
