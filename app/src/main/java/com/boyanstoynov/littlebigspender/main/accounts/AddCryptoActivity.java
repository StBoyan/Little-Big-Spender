package com.boyanstoynov.littlebigspender.main.accounts;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
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
 * Add crypto activity.
 *
 * @author Boyan Stoynov
 */
public class AddCryptoActivity extends BaseActivity {

    @BindView(R.id.spinner_crypto_name) Spinner cryptoNameSpinner;
    @BindView(R.id.numberInput_crypto_balance) EditText balanceInput;
    //TODO refactor to use same toolbar in add category and add transaction
    private AccountDao accountDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountDao = getRealmManager().createAccountDao();
        final RealmResults<Account> accounts = accountDao.getAllCrypto();

        final List<String> cryptoList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.crypto_codes)));


        final ArrayAdapter<String> cryptoAdapter = new ArrayAdapter<>(this,
                        R.layout.item_spinner, cryptoList);

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

    @OnClick(R.id.button_addItem_add)
    public void addCryptoAccount() {
        createCryptoAccount();
        Toast.makeText(getApplicationContext(), R.string.addCrypto_add_toast, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @OnClick(R.id.button_addItem_cancel)
    public void cancelAddCryptoAccount() {
        Toast.makeText(getApplicationContext(), R.string.addCrypto_discard_toast, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    private void createCryptoAccount() {
        Account newCryptoAccount = new Account();
        newCryptoAccount.setName((String)cryptoNameSpinner.getSelectedItem());
        newCryptoAccount.setBalance(new BigDecimal(balanceInput.getText().toString()));
        newCryptoAccount.setCryptoData(new CryptoData());
        newCryptoAccount.setFiatValue(new BigDecimal(0));
        newCryptoAccount.setLastUpdated(0);
        accountDao.save(newCryptoAccount);
    }

}
