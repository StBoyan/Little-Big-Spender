package com.boyanstoynov.littlebigspender.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.main.MainActivity;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

/**
 * Introduction activity launched upon first start of application.
 * Consists of welcome screen and a feature overview.
 *
 * @author Boyan Stoynov
 */
public class IntroActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SliderPage welcomePage = new SliderPage();
        welcomePage.setTitle(getResources().getString(R.string.intro_welcome_title));
        welcomePage.setDescription(getResources().getString(R.string.intro_welcome_text));
        welcomePage.setImageDrawable(R.drawable.ic_logo);
        welcomePage.setBgColor(getResources().getColor(R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(welcomePage));

        SliderPage featurePage1 = new SliderPage();
        featurePage1.setTitle(getResources().getString(R.string.intro_feature1_title));
        featurePage1.setDescription(getResources().getString(R.string.intro_feature1_text));
        featurePage1.setImageDrawable(R.drawable.ic_feature1);
        featurePage1.setBgColor(getResources().getColor(R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(featurePage1));

        SliderPage featurePage2 = new SliderPage();
        featurePage2.setTitle(getResources().getString(R.string.intro_feature2_title));
        featurePage2.setDescription(getResources().getString(R.string.intro_feature2_text));
        featurePage2.setImageDrawable(R.drawable.ic_feature2);
        featurePage2.setBgColor(getResources().getColor(R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(featurePage2));

        SliderPage featurePage3 = new SliderPage();
        featurePage3.setTitle(getResources().getString(R.string.intro_feature3_title));
        featurePage3.setDescription(getResources().getString(R.string.intro_feature3_text));
        featurePage3.setImageDrawable(R.drawable.ic_feature3);
        featurePage3.setBgColor(getResources().getColor(R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(featurePage3));

        showSkipButton(false);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
