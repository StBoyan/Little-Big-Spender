package com.boyanstoynov.littlebigspender.recurring;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.boyanstoynov.littlebigspender.BaseFragment;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.RealmManager;
import com.boyanstoynov.littlebigspender.db.dao.RecurringDao;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Recurring;

import java.util.List;

import butterknife.BindView;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Controller for Recurring fragment.
 *
 * @author Boyan Stoynov
 */
public class RecurringFragment extends BaseFragment {

    @BindView(R.id.recyclerview_recurring) RecyclerView recyclerView;

    private RecurringAdapter adapter;
    private RealmResults<Recurring> recurringRealmResults;
    private RecurringDao recurringDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        initViews();
        loadRecurringList();

        return view;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recurring;
    }

    protected void setCategoryType(Category.Type type) {
        //TODO remove temp solution
        RealmManager rm = new RealmManager();
        rm.open();
        recurringDao = rm.createRecurringDao();

        if (type == Category.Type.INCOME)
            recurringRealmResults = recurringDao.getAllIncomeRecurringTransactions();
        else
            recurringRealmResults = recurringDao.getAllExpenseRecurringTransactions();
    }

    private void initViews() {
        adapter = new RecurringAdapter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void loadRecurringList() {
        if (recurringRealmResults == null) {
            throw new IllegalStateException("Categories type unspecified. Call setCategoryType() first.");
        }

        recurringRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Recurring>>() {
            @Override
            public void onChange(RealmResults<Recurring> recurrings) {
                populateRecyclerView(recurrings);
            }
        });

        populateRecyclerView(recurringRealmResults);
    }

    private void populateRecyclerView(List<Recurring> recurringList) {
        adapter.setData(recurringList);
    }

    //TODO could try to extract alert dialog to a helper class with getResponse() to check for yes/no
    public void onDeleteButtonClicked(final Recurring recurring) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.recurring_warning_delete_message);
        builder.setIcon(R.drawable.ic_warning);
        builder.setPositiveButton(R.string.all_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                recurringDao.delete(recurring);
                Toast.makeText(getContext(), R.string.recurring_delete_toast, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.all_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
