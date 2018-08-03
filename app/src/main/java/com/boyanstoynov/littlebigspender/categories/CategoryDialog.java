package com.boyanstoynov.littlebigspender.categories;

import android.widget.EditText;
import android.widget.Spinner;

import com.boyanstoynov.littlebigspender.BaseEditorDialog;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Category;

import butterknife.BindView;

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
        return R.layout.dialog_category;
    }

    @Override
    protected boolean onPositiveClick() {
        //TODO implement validation
        item.setName(nameInput.getText().toString());

        return true;
    }

    @Override
    protected void populateDialog() {
        //TODO remove magic numbers
        nameInput.setText(item.getName());
        if (item.getType() == Category.Type.INCOME)
            typeSpinner.setSelection(0);
        else
            typeSpinner.setSelection(1);

        typeSpinner.setEnabled(false);
    }
}
