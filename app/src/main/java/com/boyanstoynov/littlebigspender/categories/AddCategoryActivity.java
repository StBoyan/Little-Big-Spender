package com.boyanstoynov.littlebigspender.categories;


import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Category;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Controller for Add Category activity.
 *
 * @author Boyan Stoynov
 */
public class AddCategoryActivity extends BaseActivity {

    @BindView(R.id.textinput_addcategory_name) EditText categoryNameInput;
    @BindView(R.id.spinner_addcategory_type) Spinner categoryTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_category;
    }

    @OnClick(R.id.button_addcategory_add)
    public void addCategory() {
        //TODO need to validate input here
        createCategory();
        Toast.makeText(this, R.string.addcategory_add_toast, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @OnClick(R.id.button_addcategory_cancel)
    public void cancelAddCategory() {
        Toast.makeText(this, R.string.addcategory_discard_toast, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    private void createCategory() {
        Category newCategory = new Category();
        newCategory.setName(categoryNameInput.getText().toString());
        //TODO remove magic number for itemPosition
        if (categoryTypeSpinner.getSelectedItemPosition() == 0)
            newCategory.setType(Category.Type.INCOME);
        else
            newCategory.setType(Category.Type.EXPENSE);
        getRealmManager().createCategoryDao().save(newCategory);
    }
}
