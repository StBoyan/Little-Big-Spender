package com.boyanstoynov.littlebigspender.main.accounts;

import android.text.InputFilter;
import android.widget.EditText;

import com.boyanstoynov.littlebigspender.BaseEditorDialog;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.util.DecimalDigitsInputFilter;

import java.math.BigDecimal;

import butterknife.BindView;

/**
 * Edit account dialog implementation.
 *
 * @author Boyan Stoynov
 */
public class EditAccountDialog extends BaseEditorDialog<Account> {

    @BindView(R.id.textInput_account_name) EditText nameInput;
    @BindView(R.id.numberInput_account_balance) EditText balanceInput;

    private final int BALANCE_DIGITS_BEFORE_ZERO_FILTER = 7;
    private final int BALANCE_DIGITS_AFTER_ZERO_FILTER = 2;


    @Override
    protected int getTitleResource() {
        return R.string.accounts_editDialog_title;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_account_edit;
    }

    @Override
    protected boolean onPositiveClick() {
        item.setName(nameInput.getText().toString());
        item.setBalance(new BigDecimal(balanceInput.getText().toString()));
        //TODO implement validation
        return true;
    }

    @Override
    protected void populateDialog() {
        nameInput.setText(item.getName());
        balanceInput.setText(item.getBalance().toString());

        balanceInput.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(BALANCE_DIGITS_BEFORE_ZERO_FILTER, BALANCE_DIGITS_AFTER_ZERO_FILTER)});
    }
}
