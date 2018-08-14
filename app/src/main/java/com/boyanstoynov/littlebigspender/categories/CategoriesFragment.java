package com.boyanstoynov.littlebigspender.categories;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boyanstoynov.littlebigspender.BaseFragment;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.dao.CategoryDao;
import com.boyanstoynov.littlebigspender.db.model.Category;

import java.util.List;

import butterknife.BindView;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

import static com.boyanstoynov.littlebigspender.categories.CategoriesActivity.CATEGORY_TYPE_KEY;
import static com.boyanstoynov.littlebigspender.categories.CategoriesActivity.INCOME_TYPE_VALUE;

/**
 * Categories fragment which contains the selected category
 * within the CategoriesActivity. Handles creating and managing the
 * RecyclerView and updating its information.
 *
 * @author Boyan Stoynov
 */
public class CategoriesFragment extends BaseFragment {

    @BindView(R.id.recyclerview_categories) RecyclerView recyclerView;

    private CategoriesAdapter adapter;
    private RealmResults<Category> categoriesRealmResults;
    private Bundle categoryType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Get bundle and store it so that it can be used
        upon configuration change as well */
        categoryType = getArguments();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        setCategoryType();
        initViews();
        loadCategoryList();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        categoriesRealmResults.removeAllChangeListeners();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_categories;
    }

    /**
     * Set the type of categories that this fragment will display.
     */
    private void setCategoryType() {
        CategoryDao categoryDao = getRealmManager().createCategoryDao();

        if (categoryType.getString(CATEGORY_TYPE_KEY).equals(INCOME_TYPE_VALUE))
            categoriesRealmResults = categoryDao.getAllIncomeCategories();
        else
            categoriesRealmResults = categoryDao.getAllExpenseCategories();
    }

    /**
     * Initialise the adapter and recycler view.
     */
    private void initViews() {
        adapter = new CategoriesAdapter((CategoriesActivity)getActivity());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    /**
     * Loads the category list for the first time and set a database
     * change listener.
     */
    private void loadCategoryList() {
        categoriesRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Category>>() {
            @Override
            public void onChange(@NonNull RealmResults<Category> categories) {
                populateRecyclerView(categories);
            }
        });

        populateRecyclerView(categoriesRealmResults);
    }

    /**
     * Populates the RecyclerView with list of categories.
     * @param categoriesList list of categories
     */
    private void populateRecyclerView(List<Category> categoriesList) {
        adapter.setData(categoriesList);
    }
}
