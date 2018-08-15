package com.boyanstoynov.littlebigspender.main.accounts;

import android.text.InputFilter;
import android.widget.EditText;

import com.boyanstoynov.littlebigspender.BaseEditorDialog;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.dao.AccountDao;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.util.DecimalDigitsInputFilter;

import java.math.BigDecimal;

import butterknife.BindView;

import static com.boyanstoynov.littlebigspender.util.Constants.ACCOUNT_NAME_MAX_LENGTH;
import static com.boyanstoynov.littlebigspender.util.Constants.CRYPTO_DIGITS_AFTER_ZERO_FILTER;
import static com.boyanstoynov.littlebigspender.util.Constants.CRYPTO_DIGITS_BEFORE_ZERO_FILTER;
import static com.boyanstoynov.littlebigspender.util.Constants.FIAT_DIGITS_AFTER_ZERO_FILTER;
import static com.boyanstoynov.littlebigspender.util.Constants.FIAT_DIGITS_BEFORE_ZERO_FILTER;

/**
 * Edit category dialog implementation. Handles changing name/balance
 * and validating user input. Provides callback upon user confirmation.
 *
 * @author Boyan Stoynov
 */
public class EditAccountDialog extends BaseEditorDialog<Account> {

    @BindView(R.id.textInput_account_name) EditText nameInput;
    @BindView(R.id.numberInput_account_balance) EditText balanceInput;

    private AccountDao accountDao;

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
        nameInput.setText(nameInput.getText().toString().trim());

        // If account is crypto, do not validate name
        if (item.isCrypto() && isBalanceValid()) {
            item.setName(nameInput.getText().toString());
            item.setBalance(new BigDecimal(balanceInput.getText().toString()));
            return true;
        }
        else if (!item.isCrypto() && isNameValid() && isBalanceValid()) {
            item.setName(nameInput.getText().toString());
            item.setBalance(new BigDecimal(balanceInput.getText().toString()));
            return true;
        }
        else
            return false;
    }

    @Override
    protected void populateDialog() {
        nameInput.setText(item.getName());

        if (item.isCrypto()) {
            //Disable name input since Crypto account names cannot be changed
            nameInput.setEnabled(false);
            balanceInput.setText(getCryptoFormatter().format(item.getBalance()));
            balanceInput.setFilters(new InputFilter[] {
                    new DecimalDigitsInputFilter(
                            CRYPTO_DIGITS_BEFORE_ZERO_FILTER, CRYPTO_DIGITS_AFTER_ZERO_FILTER)});
        }
        else {
            balanceInput.setText(getFiatFormatter().format(item.getBalance()));
            balanceInput.setFilters(new InputFilter[] {
                    new DecimalDigitsInputFilter(
                            FIAT_DIGITS_BEFORE_ZERO_FILTER, FIAT_DIGITS_AFTER_ZERO_FILTER)});
        }

        accountDao = getRealmManager().createAccountDao();
    }

    /**
     * Checks name user input and return boolean whether it is
     * valid or not. If invalid display error message to user.
     * @return boolean whether name is valid
     */
    private boolean isNameValid() {
        String name = nameInput.getText().toString();

        if (name.length() == 0) {
            nameInput.setError(getResources().getString(R.string.all_blank_field_error));
            return false;
        }

        if (name.length() > ACCOUNT_NAME_MAX_LENGTH) {
            nameInput.setError(getResources().getString(R.string.all_name_long_error));
            return false;
        }

        String[] cryptoNames = getResources().getStringArray(R.array.crypto_names);
        for (String cryptoName : cryptoNames) {
            if (name.equals(cryptoName)) {
                nameInput.setError(getResources().getString(R.string.accounts_crypto_name_error));
                return false;
            }
        }

        Account duplicateAccount = accountDao.getByName(name);
        if (duplicateAccount != null && !duplicateAccount.getName().equals(item.getName())) {
            nameInput.setError(getResources().getString(R.string.accounts_name_exists_error));
            return false;
        }

        return true;
    }

    /**
     * Check balance user input and return boolean whether it is
     * valid or not. If invalid display error message to user.
     * @return boolean whether valid or not
     */
    public boolean isBalanceValid() {
        if (balanceInput.getText().toString().equals("")) {
            balanceInput.setError(getResources().getString(R.string.all_blank_field_error));
            return false;
        }

        // If name input is disabled, then account is crypto, therefore apply crypto balance rules
        if (!nameInput.isEnabled())
            if (new BigDecimal(balanceInput.getText().toString()).compareTo(BigDecimal.ZERO) < 1) {
                balanceInput.setError(getResources().getString(R.string.all_cannot_be_zero_error));
                return false;
            }

        return true;
    }
}
