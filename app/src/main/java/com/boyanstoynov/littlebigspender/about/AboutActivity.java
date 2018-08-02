package com.boyanstoynov.littlebigspender.about;

import android.os.Bundle;

import com.boyanstoynov.littlebigspender.R;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsActivity;

/**
 * About Activity that displays the app's description, license
 * information, and a list of the libraries used.
 *
 * @author Boyan Stoynov
 */
public class AboutActivity extends LibsActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        LibsBuilder builder = new LibsBuilder()
                .withActivityTitle(getResources().getString(R.string.all_about))
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