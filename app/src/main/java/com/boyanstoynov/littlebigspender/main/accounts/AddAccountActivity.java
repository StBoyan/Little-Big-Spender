package com.boyanstoynov.littlebigspender.main.accounts;

import android.os.Bundle;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.Toast;

import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.dao.AccountDao;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.util.DecimalDigitsInputFilter;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;

import static com.boyanstoynov.littlebigspender.util.Constants.ACCOUNT_NAME_MAX_LENGTH;
import static com.boyanstoynov.littlebigspender.util.Constants.FIAT_DIGITS_AFTER_ZERO_FILTER;
import static com.boyanstoynov.littlebigspender.util.Constants.FIAT_DIGITS_BEFORE_ZERO_FILTER;

/**
 * Controller for Add account activity. Takes in user input
 * and validates it and creates new accounts (fiat).
 *
 * @author Boyan Stoynov
 */
public class AddAccountActivity extends BaseActivity {

    @BindView(R.id.textInput_account_name) EditText accountNameInput;
    @BindView(R.id.numberInput_account_balance) EditText balanceInput;

    private AccountDao accountDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountDao = getRealmManager().createAccountDao();
        balanceInput.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(
                FIAT_DIGITS_BEFORE_ZERO_FILTER, FIAT_DIGITS_AFTER_ZERO_FILTER)});
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_account;
    }

    /**
     * Validate input and inform user of outcome, going back
     * to previous activity if successful.
     */
    @OnClick(R.id.button_addItem_add)
    public void addAccount() {
        accountNameInput.setText(accountNameInput.getText().toString().trim());

        if (isNameValid() && isBalanceValid()) {
            createAccount();
            Toast.makeText(getApplicationContext(), R.string.accounts_add_addToast, Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    /**
     * Discard new account and go back.
     */
    @OnClick(R.id.button_addItem_cancel)
    public void cancelAddAccount() {
        Toast.makeText(getApplicationContext(), R.string.accounts_add_discardToast, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    /**
     * Create new fiat account and save it to database.
     */
    private void createAccount() {
        Account newAccount = new Account();
        newAccount.setName(accountNameInput.getText().toString());
        newAccount.setBalance(new BigDecimal(balanceInput.getText().toString()));
        accountDao.save(newAccount);
    }

    /**
     * Checks name user input and return boolean whether it is
     * valid or not. If invalid display error message to user.
     * @return boolean whether name is valid
     */
    private boolean isNameValid() {
        String name = accountNameInput.getText().toString();

        if (name.length() == 0) {
            accountNameInput.setError(getResources().getString(R.string.all_blank_field_error));
            return false;
        }

        if (name.length() > ACCOUNT_NAME_MAX_LENGTH) {
            accountNameInput.setError(getResources().getString(R.string.all_name_long_error));
            return false;
        }

        String[] cryptoNames = getResources().getStringArray(R.array.crypto_names);
        for (String cryptoName : cryptoNames) {
            if (name.equals(cryptoName)) {
                accountNameInput.setError(getResources().getString(R.string.accounts_crypto_name_error));
                return false;
            }
        }

        Account duplicateAccount = accountDao.getByName(name);
        if (duplicateAccount != null) {
            accountNameInput.setError(getResources().getString(R.string.accounts_name_exists_error));
            return false;
        }

        return true;
    }

    /**
     * Check balance user input and return boolean whether it is
     * valid or not. If invalid display error message to user.
     * @return boolean whether valid or not
     */
    private boolean isBalanceValid() {
        if (balanceInput.getText().toString().equals("")) {
            balanceInput.setError(getString(R.string.all_blank_field_error));
            return false;
        }
        else
            return true;
    }
}

