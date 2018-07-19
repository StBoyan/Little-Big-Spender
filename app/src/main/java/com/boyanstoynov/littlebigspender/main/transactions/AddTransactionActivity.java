package com.boyanstoynov.littlebigspender.main.transactions;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Controller for Add transaction activity.
 *
 * @author Boyan Stoynov
 */
public class AddTransactionActivity extends BaseActivity {

    @BindView(R.id.toolbar_addtransaction) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_transaction;
    }
    //TODO consider refactoring layout to have 2 fragments which have the same layout but fill in com.boyanstoynov.littlebigspender.categories differently
    @OnClick(R.id.button_addtransaction_add)
    public void addTransaction() {
        Toast.makeText(getApplicationContext(), "Transaction added", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @OnClick(R.id.button_addtransaction_cancel)
    public void cancelTransaction() {
        Toast.makeText(getApplicationContext(), "Transaction discarded", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }
}
