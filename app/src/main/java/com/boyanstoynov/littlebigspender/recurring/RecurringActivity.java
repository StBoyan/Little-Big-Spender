package com.boyanstoynov.littlebigspender.recurring;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.R;

import butterknife.BindView;

/**
 * Controller for recurring transactions activity.
 *
 * @author Boyan Stoynov
 */
public class RecurringActivity extends BaseActivity {
    @BindView(R.id.toolbar_recurring)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_recurring);

    }

    /**
     * @inheritDoc
     */
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_recurring;
    }
}
