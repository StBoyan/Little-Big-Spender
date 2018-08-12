package com.boyanstoynov.littlebigspender.main.accounts;

import android.os.Bundle;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.Toast;

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.util.DecimalDigitsInputFilter;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;

import static com.boyanstoynov.littlebigspender.util.Constants.FIAT_DIGITS_AFTER_ZERO_FILTER;
import static com.boyanstoynov.littlebigspender.util.Constants.FIAT_DIGITS_BEFORE_ZERO_FILTER;

/**
 * Controller for Add account activity.
 *
 * @author Boyan Stoynov
 */
public class AddAccountActivity extends BaseActivity {

    @BindView(R.id.textInput_account_name) EditText accountNameInput;
    @BindView(R.id.numberInput_account_balance) EditText balanceInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        balanceInput.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(FIAT_DIGITS_BEFORE_ZERO_FILTER, FIAT_DIGITS_AFTER_ZERO_FILTER)});
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_account;
    }

    @OnClick(R.id.button_addAccount_add)
    public void addAccount() {
        //TODO need to validate input here
        createAccount();
        Toast.makeText(getApplicationContext(), R.string.addAccount_add_toast, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @OnClick(R.id.button_addAccount_cancel)
    public void cancelAddAccount() {
        Toast.makeText(getApplicationContext(), R.string.addAccount_discard_toast, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    private void createAccount() {
        Account newAccount = new Account();
        newAccount.setName(accountNameInput.getText().toString());
        newAccount.setBalance(new BigDecimal(balanceInput.getText().toString()));
        getRealmManager().createAccountDao().save(newAccount);
    }
}
