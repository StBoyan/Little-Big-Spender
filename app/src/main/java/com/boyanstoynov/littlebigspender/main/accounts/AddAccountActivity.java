package com.boyanstoynov.littlebigspender.main.accounts;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.db.dao.AccountDao;
import com.boyanstoynov.littlebigspender.db.model.Account;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Controller for Add account activity.
 *
 * @author Boyan Stoynov
 */
public class AddAccountActivity extends BaseActivity {

    @BindView(R.id.textinput_addaccount_name) EditText accountNameInput;
    @BindView(R.id.numberinput_addaccount_balance) EditText balanceInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_account;
    }

    @OnClick(R.id.button_addaccount_add)
    public void addAccount() {
        // TODO either refactor or make realmmanager protected in base activity to be accessable
        // TODO actually may be better to just store the accountDAO reference
        // TODO but need to log and see if the super onDestroy closes realm
        Account newAccount = new Account();
        newAccount.setName(accountNameInput.getText().toString());
        newAccount.setBalance(new BigDecimal(balanceInput.getText().toString()));
        AccountDao ad = getRealmManager().createAccountDao();
        ad.save(newAccount);
        Toast.makeText(getApplicationContext(), "Account added", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @OnClick(R.id.button_addaccount_cancel)
    public void cancelAddAccount() {
        Toast.makeText(getApplicationContext(), "Account discarded", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }
}
