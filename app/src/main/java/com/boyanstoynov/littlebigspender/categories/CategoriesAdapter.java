package com.boyanstoynov.littlebigspender.categories;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boyanstoynov.littlebigspender.BaseRecyclerAdapter;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Category;

import butterknife.BindView;

/**
 * RecyclerView adapter for Category entities.
 *
 * @author Boyan Stoynov
 */
public class CategoriesAdapter extends BaseRecyclerAdapter<Category>{

    CategoriesAdapter(RecyclerViewListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(parent, this);
    }

    public static class CategoryViewHolder extends BaseRecyclerAdapter.ViewHolder<Category> {

        @BindView(R.id.text_itemCategory_category) TextView textCategory;

        CategoryViewHolder(ViewGroup parent, CategoriesAdapter adapter) {
            super(parent, R.layout.item_category, adapter);
        }

        @Override
        protected void setItemPresentation(Category category) {
            textCategory.setText(category.getName());
        }
    }
}
