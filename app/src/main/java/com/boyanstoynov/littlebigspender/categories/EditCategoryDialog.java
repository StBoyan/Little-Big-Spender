package com.boyanstoynov.littlebigspender.categories;

import android.widget.EditText;
import android.widget.Spinner;

import com.boyanstoynov.littlebigspender.BaseEditorDialog;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.dao.CategoryDao;
import com.boyanstoynov.littlebigspender.db.model.Category;

import butterknife.BindView;

import static com.boyanstoynov.littlebigspender.util.Constants.CATEGORY_NAME_MAX_LENGTH;
import static com.boyanstoynov.littlebigspender.util.Constants.EXPENSE_POSITION;
import static com.boyanstoynov.littlebigspender.util.Constants.INCOME_POSITION;

/**
 * Edit category dialog implementation. Handles changing name
 * and validating name user input.
 *
 * @author Boyan Stoynov
 */
public class EditCategoryDialog extends BaseEditorDialog<Category> {

    @BindView(R.id.textInput_category_name) EditText nameInput;
    @BindView(R.id.spinner_category_type) Spinner typeSpinner;

    CategoryDao categoryDao;

    @Override
    protected int getTitleResource() {
        return R.string.categories_editDialog_title;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_category_edit;
    }

    @Override
    protected boolean onPositiveClick() {
        if (isNameValid()) {
            item.setName(nameInput.getText().toString());
            return true;
        }
        else
            return false;
    }

    @Override
    protected void populateDialog() {
        nameInput.setText(item.getName());
        if (item.getType() == Category.Type.INCOME)
            typeSpinner.setSelection(INCOME_POSITION);
        else
            typeSpinner.setSelection(EXPENSE_POSITION);

        typeSpinner.setEnabled(false);

        categoryDao = getRealmManager().createCategoryDao();
    }

    /**
     * Checks name user input and return boolean whether it is
     * valid or not. If invalid display error message to user.
     *
     * @return boolean whether name is valid
     */
    private boolean isNameValid() {
        String name = nameInput.getText().toString();

        if (name.length() == 0) {
            nameInput.setError(getResources().getString(R.string.all_blank_field_error));
            return false;
        }
        if (name.length() > CATEGORY_NAME_MAX_LENGTH) {
            nameInput.setError(getResources().getString(R.string.categories_name_long_error));
            return false;
        }
        Category duplicateCategory = categoryDao.getByName(name);
        if (duplicateCategory != null) {
            nameInput.setError(getResources().getString(R.string.categories_name_exist_error));
            return false;
        }

        return true;
    }
}
