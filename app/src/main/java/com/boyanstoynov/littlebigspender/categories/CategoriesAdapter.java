package com.boyanstoynov.littlebigspender.categories;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Category;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Recycler View adapter to provide access to Category
 * entities and dynamically make View for each item.
 *
 * @author Boyan Stoynov
 */
public class CategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    //TODO consider a common adapater superclass for all shared attributes
    private List<Category> categoriesDataSet;
    private CategoriesFragment categoriesFragment;

    public CategoriesAdapter(CategoriesFragment fragment) {
        categoriesDataSet = new ArrayList<>();
        categoriesFragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view, categoriesFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CategoryViewHolder viewHolder = (CategoryViewHolder) holder;
        Category cat = categoriesDataSet.get(position);

        viewHolder.textCategory.setText(cat.getName());
    }

    @Override
    public int getItemCount() {
        return categoriesDataSet.size();
    }

    public void setData(@NonNull List<Category> categories) {
        categoriesDataSet.clear();
        categoriesDataSet.addAll(categories);
    }

    /**
     * Static ViewHolder inner class to describe how data
     * in each View is displayed.
     */
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
     //TODO consider making a common ViewHolder parent if all of them will be expandable (and have butterknife)
        @BindView(R.id.text_itemcategory_category) TextView textCategory;
        @BindView(R.id.button_itemcategory_delete) Button deleteButton;
        @BindView(R.id.button_itemcategory_edit) Button editButton;
        @BindView(R.id.divider_itemcategory) View divider;

        /* Whether view is clicked */
        boolean isExpanded;
        CategoriesFragment fragment;

        public CategoryViewHolder(View v, CategoriesFragment fragment) {
            super(v);
            // Bind ButterKnife to View
            ButterKnife.bind(this, v);
            this.fragment = fragment;

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isExpanded) {
                        editButton.setVisibility(View.VISIBLE);
                        deleteButton.setVisibility(View.VISIBLE);
                        divider.setVisibility(View.VISIBLE);
                        isExpanded = true;
                    }
                    else {
                        editButton.setVisibility(View.GONE);
                        deleteButton.setVisibility(View.GONE);
                        divider.setVisibility(View.GONE);
                        isExpanded = false;
                    }
                }
            });
        }

        @OnClick(R.id.button_itemcategory_delete)
        public void onDeleteButtonClicked() {
            fragment.onDeleteButtonClicked(textCategory.getText().toString());
        }
    }
}
