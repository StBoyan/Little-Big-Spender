package com.boyanstoynov.littlebigspender.about;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.BaseActivity;

import butterknife.BindView;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.toolbar_about)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//TODO consider putting actionbar for drawer items in a common class in order to avoid duplication
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_about);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_about;
    }
}
