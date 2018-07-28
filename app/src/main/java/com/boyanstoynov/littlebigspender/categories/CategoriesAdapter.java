package com.boyanstoynov.littlebigspender.categories;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.boyanstoynov.littlebigspender.BaseRecyclerAdapter;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Category;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * RecyclerView adapter for Category entities.
 *
 * @author Boyan Stoynov
 */
public class CategoriesAdapter extends BaseRecyclerAdapter<Category, CategoriesFragment>{

    CategoriesAdapter(CategoriesFragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(parent, fragment);
    }

    public static class CategoryViewHolder extends BaseRecyclerAdapter.ViewHolder<Category, CategoriesFragment> {

        @BindView(R.id.text_itemcategory_category) TextView textCategory;
        @BindView(R.id.button_itemcategory_delete) Button deleteButton;
        @BindView(R.id.button_itemcategory_edit) Button editButton;
        @BindView(R.id.divider_itemcategory) View divider;

        boolean isExpanded;

        CategoryViewHolder(ViewGroup parent, CategoriesFragment fragment) {
            super(parent, R.layout.item_category, fragment);

            itemView.setOnClickListener(new View.OnClickListener() {
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

        @Override
        protected void setItemPresentation(Category category) {
            textCategory.setText(category.getName());
        }

        @OnClick(R.id.button_itemcategory_delete)
        public void onDeleteButtonClicked() {
            fragment.onDeleteButtonClicked(item);
        }
    }
}
