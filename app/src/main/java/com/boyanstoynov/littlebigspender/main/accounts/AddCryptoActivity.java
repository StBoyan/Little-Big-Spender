package com.boyanstoynov.littlebigspender.main.accounts;

import android.os.Bundle;
import android.text.InputFilter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.dao.AccountDao;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.db.model.CryptoData;
import com.boyanstoynov.littlebigspender.util.DecimalDigitsInputFilter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.RealmResults;

import static com.boyanstoynov.littlebigspender.util.Constants.CRYPTO_DIGITS_AFTER_ZERO_FILTER;
import static com.boyanstoynov.littlebigspender.util.Constants.CRYPTO_DIGITS_BEFORE_ZERO_FILTER;

/**
 * Controller for Add Crypto Account activity. Takes in user input
 * and validates it and creates new accounts (crypto).
 *
 * @author Boyan Stoynov
 */
public class AddCryptoActivity extends BaseActivity {

    @BindView(R.id.spinner_crypto_name) Spinner cryptoNameSpinner;
    @BindView(R.id.numberInput_crypto_balance) EditText balanceInput;

    private AccountDao accountDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountDao = getRealmManager().createAccountDao();
        final RealmResults<Account> accounts = accountDao.getAllCrypto();
        final List<String> cryptoList = new ArrayList<>(
                Arrays.asList(getResources().getStringArray(R.array.crypto_names)));
        final ArrayAdapter<String> cryptoAdapter = new ArrayAdapter<>(this,
                        R.layout.item_spinner, cryptoList);
        // Remove crypto accounts from spinner that already exist
        for (Account account : accounts) {
            cryptoList.remove(account.getName());
        }
        cryptoNameSpinner.setAdapter(cryptoAdapter);

        balanceInput.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(
                CRYPTO_DIGITS_BEFORE_ZERO_FILTER, CRYPTO_DIGITS_AFTER_ZERO_FILTER)});
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_crypto;
    }

    /**
     * Validate input and inform user of outcome, going back
     * to previous activity if successful.
     */
    @OnClick(R.id.button_addItem_add)
    public void addCryptoAccount() {
        if (balanceIsValid()) {
            createCryptoAccount();
            Toast.makeText(getApplicationContext(), R.string.accounts_cryptoAdd_addToast, Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    /**
     * Discard new crypto account and go back.
     */
    @OnClick(R.id.button_addItem_cancel)
    public void cancelAddCryptoAccount() {
        Toast.makeText(getApplicationContext(), R.string.accounts_cryptoAdd_discardToast, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    /**
     * Create new crypto account and save it to database.
     */
    private void createCryptoAccount() {
        Account newCryptoAccount = new Account();
        newCryptoAccount.setName((String)cryptoNameSpinner.getSelectedItem());
        newCryptoAccount.setBalance(new BigDecimal(balanceInput.getText().toString()));
        newCryptoAccount.setCryptoData(new CryptoData());
        accountDao.save(newCryptoAccount);
    }

    /**
     * Checks balance user input and return boolean whether it is
     * valid or not. If invalid display error message to user.
     * @return boolean whether valid or not
     */
    private boolean balanceIsValid() {
        if (balanceInput.getText().toString().equals("")) {
            balanceInput.setError(getResources().getString(R.string.all_blank_field_error));
            return false;
        }

        if (new BigDecimal(balanceInput.getText().toString()).compareTo(BigDecimal.ZERO) < 1) {
            balanceInput.setError(getResources().getString(R.string.all_cannot_be_zero_error));
            return false;
        }

        return true;
    }
}
