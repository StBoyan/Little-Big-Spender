package com.boyanstoynov.littlebigspender;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boyanstoynov.littlebigspender.db.dao.RealmManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Base class for Fragments. Binds and unbinds ButterKnife
 * and provides access to parent activity's RealmManager
 * instance so subclasses.
 *
 * @author Boyan Stoynov
 */
public abstract class BaseFragment extends Fragment {

    private Unbinder unbinder;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);
        // Bind ButterKnife to fragment
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unbind ButterKnife from fragment
        unbinder.unbind();
    }

    /**
     * Gets the id of the layout that will fill the activity.
     * @return int layout id
     */
    protected abstract int getLayoutResource();

    /**
     * Gets the RealmManager from parent activity.
     * @return RealmManager object
     */
    protected final RealmManager getRealmManager() {
        return ((BaseActivity)getActivity()).getRealmManager();
    }
}
