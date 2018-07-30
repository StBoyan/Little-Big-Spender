package com.boyanstoynov.littlebigspender.about;

import android.os.Bundle;

import com.boyanstoynov.littlebigspender.R;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsActivity;

/**
 * Sets up the about activity with LibsBuilder from
 * AboutLibraries library.
 *
 * @author Boyan Stoynov
 */
public class AboutActivity extends LibsActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        LibsBuilder builder = new LibsBuilder()
                .withActivityTitle(getResources().getString(R.string.all_about))
                .withLibraries("Currency Picker")
                .withExcludedLibraries("constraint_layout", "support_annotations", "appintro", "appintro")
                .withLicenseShown(true)
                .withActivityTheme(R.style.AppTheme)
                .withAboutAppName(getResources().getString(R.string.app_name))
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withAboutDescription(getResources().getString(R.string.about_description))
                .withAboutSpecial1(getResources().getString(R.string.about_license))
                .withAboutSpecial1Description(getResources().getString(R.string.about_license_description));

        setIntent(builder.intent(this));
        super.onCreate(savedInstanceState);
    }
}