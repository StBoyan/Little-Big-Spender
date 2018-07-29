package com.boyanstoynov.littlebigspender.db.dao;

import android.support.annotation.NonNull;

import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Recurring;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Data access object that defines queries for Recurring
 * Transaction entities.
 *
 * @author Boyan Stoynov
 */
public class RecurringDao extends BaseDao<Recurring> {

    public RecurringDao(@NonNull Realm realm) {
        super(realm);
    }

    public RealmResults<Recurring> getAllIncomeRecurringTransactions() {
        return realm.where(Recurring.class).equalTo("category.type", Category.Type.INCOME.toString()).findAll();
    }

    public RealmResults<Recurring> getAllExpenseRecurringTransactions() {
        return realm.where(Recurring.class).equalTo("category.type", Category.Type.EXPENSE.toString()).findAll();
    }
}
