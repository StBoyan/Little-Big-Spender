package com.boyanstoynov.littlebigspender.ui;

import android.os.Bundle;
import android.widget.Toast;

import com.boyanstoynov.littlebigspender.R;

import butterknife.OnClick;

public class AddAccountActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_account;
    }

    @OnClick(R.id.button_addaccount_add)
    public void addAccount() {
        Toast.makeText(getApplicationContext(), "Account added", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @OnClick(R.id.button_addaccount_cancel)
    public void cancelAddAccount() {
        Toast.makeText(getApplicationContext(), "Account discarded", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }
}
