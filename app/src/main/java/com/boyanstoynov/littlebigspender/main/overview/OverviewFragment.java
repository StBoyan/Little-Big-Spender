package com.boyanstoynov.littlebigspender.main.overview;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.BaseFragment;

/**
 * Controller for overview fragment of main screen.
 *
 * @author Boyan Stoynov
 */
public class OverviewFragment extends BaseFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(R.string.all_overview);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_overview;
    }
}
