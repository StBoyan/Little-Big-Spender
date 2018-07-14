package com.boyanstoynov.littlebigspender.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.boyanstoynov.littlebigspender.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("Welcome to Little Big Spender", "Track your expenses and income no matter how big or little.", R.drawable.ic_logo, getResources().getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Feature 1", "Description of a feature", 0, getResources().getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Feature 2", "Description of another feature", 0, getResources().getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Initial setup", "Set initial balances and currency", 0, getResources().getColor(R.color.colorPrimary)));

        showSkipButton(false);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
