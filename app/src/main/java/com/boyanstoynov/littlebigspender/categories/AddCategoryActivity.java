package com.boyanstoynov.littlebigspender.categories;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.dao.CategoryDao;
import com.boyanstoynov.littlebigspender.db.model.Category;

import butterknife.BindView;
import butterknife.OnClick;

import static com.boyanstoynov.littlebigspender.categories.CategoriesActivity.CATEGORY_TYPE_KEY;
import static com.boyanstoynov.littlebigspender.util.Constants.CATEGORY_NAME_MAX_LENGTH;
import static com.boyanstoynov.littlebigspender.util.Constants.INCOME_POSITION;
import static com.boyanstoynov.littlebigspender.util.Constants.EXPENSE_POSITION;

/**
 * Controller for Add Category activity. Takes in the user input
 * and validates it and creates new categories.
 *
 * @author Boyan Stoynov
 */
public class AddCategoryActivity extends BaseActivity {

    @BindView(R.id.textInput_category_name) EditText categoryNameInput;
    @BindView(R.id.spinner_category_type) Spinner categoryTypeSpinner;

    CategoryDao categoryDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryDao = getRealmManager().createCategoryDao();

        //Set spinner pos according to which tab was selected in the CategoriesActivity
        categoryTypeSpinner.setSelection(getIntent().getExtras().getInt(CATEGORY_TYPE_KEY));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_category;
    }

    /**
     * Validate input and inform user of outcome, going back
     * to previous activity if successful.
     */
    @OnClick(R.id.button_addItem_add)
    public void addCategory() {
        categoryNameInput.setText(categoryNameInput.getText().toString().trim());

        if (isNameValid()) {
            createCategory();
            Toast.makeText(this, R.string.categories_add_addToast, Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    /**
     * Discard new category and go back.
     */
    @OnClick(R.id.button_addItem_cancel)
    public void cancelAddCategory() {
        Toast.makeText(this, R.string.categories_add_discardToast, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    /**
     * Create new category and save it to the database.
     */
    private void createCategory() {
        Category newCategory = new Category();
        newCategory.setName(categoryNameInput.getText().toString());

        if (categoryTypeSpinner.getSelectedItemPosition() == INCOME_POSITION)
            newCategory.setType(Category.Type.INCOME);
        else if (categoryTypeSpinner.getSelectedItemPosition() == EXPENSE_POSITION)
            newCategory.setType(Category.Type.EXPENSE);
        categoryDao.save(newCategory);
    }

    /**
     * Checks name user input and return boolean whether it is
     * valid or not. If invalid display error message to user.
     * @return boolean whether name is valid
     */
    private boolean isNameValid() {
        String name = categoryNameInput.getText().toString();

        if (name.length() == 0) {
            categoryNameInput.setError(getResources().getString(R.string.all_blank_field_error));
            return false;
        }
        if (name.length() > CATEGORY_NAME_MAX_LENGTH) {
            categoryNameInput.setError(getResources().getString(R.string.all_name_long_error));
            return false;
        }
        Category duplicateCategory = categoryDao.getByName(name);
        if (duplicateCategory != null) {
            categoryNameInput.setError(getResources().getString(R.string.categories_name_exist_error));
            return false;
        }

        return true;
    }
}
