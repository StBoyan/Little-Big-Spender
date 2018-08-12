package com.boyanstoynov.littlebigspender.categories;

import android.widget.EditText;
import android.widget.Spinner;

import com.boyanstoynov.littlebigspender.BaseEditorDialog;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Category;

import butterknife.BindView;

import static com.boyanstoynov.littlebigspender.util.Constants.SPINNER_INCOME_POSITION;
import static com.boyanstoynov.littlebigspender.util.Constants.SPINNER_EXPENSE_POSITION;

/**
 * Edit category dialog implementation.
 *
 * @author Boyan Stoynov
 */
public class CategoryDialog extends BaseEditorDialog<Category> {

    @BindView(R.id.textInput_category_name) EditText nameInput;
    @BindView(R.id.spinner_category_type) Spinner typeSpinner;

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
        //TODO implement validation
        item.setName(nameInput.getText().toString());

        return true;
    }

    @Override
    protected void populateDialog() {
        nameInput.setText(item.getName());
        if (item.getType() == Category.Type.INCOME)
            typeSpinner.setSelection(SPINNER_INCOME_POSITION);
        else
            typeSpinner.setSelection(SPINNER_EXPENSE_POSITION);

        typeSpinner.setEnabled(false);
    }
}
