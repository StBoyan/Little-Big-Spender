package com.boyanstoynov.littlebigspender.main.accounts;

import android.widget.EditText;

import com.boyanstoynov.littlebigspender.BaseEditorDialog;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Account;

import java.math.BigDecimal;

import butterknife.BindView;

/**
 * Edit account dialog implementation.
 *
 * @author Boyan Stoynov
 */
public class AccountDialog extends BaseEditorDialog<Account> {

    @BindView(R.id.textInput_account_name) EditText nameInput;
    @BindView(R.id.numberInput_account_balance) EditText balanceInput;

    @Override
    protected int getTitleResource() {
        return R.string.accounts_editDialog_title;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_account;
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
    }
}
