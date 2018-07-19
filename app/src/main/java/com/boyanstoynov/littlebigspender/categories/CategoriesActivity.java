package com.boyanstoynov.littlebigspender.categories;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.BaseActivity;

import butterknife.BindView;

/**
 * Controller for Categories activity.
 *
 * @author Boyan Stoynov
 */
public class CategoriesActivity extends BaseActivity {

    @BindView(R.id.toolbar_categories) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_categories);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_categories;
    }
}
