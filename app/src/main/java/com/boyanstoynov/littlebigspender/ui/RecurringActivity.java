package com.boyanstoynov.littlebigspender.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.boyanstoynov.littlebigspender.R;

import butterknife.BindView;

public class RecurringActivity extends BaseActivity {
@BindView(R.id.toolbar_recurring) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_recurring);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_recurring;
    }
}
