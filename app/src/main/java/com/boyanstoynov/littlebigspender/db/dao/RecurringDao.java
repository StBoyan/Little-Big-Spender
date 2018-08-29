package com.boyanstoynov.littlebigspender.db.dao;

import android.support.annotation.NonNull;

import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Recurring;

import java.math.BigDecimal;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Data access object for queries and transactions on a
 * Recurring Transaction entity.
 *
 * @author Boyan Stoynov
 */
public class RecurringDao extends BaseDao<Recurring> {

    RecurringDao(@NonNull Realm realm) {
        super(realm);
    }

    public RealmResults<Recurring> getAllRecurringTransactions() {
        return realm.where(Recurring.class).findAll();
    }

    public RealmResults<Recurring> getAllIncomeRecurringTransactions() {
        return realm.where(Recurring.class).equalTo("category.type", Category.Type.INCOME.toString()).findAll();
    }

    public RealmResults<Recurring> getAllExpenseRecurringTransactions() {
        return realm.where(Recurring.class).equalTo("category.type", Category.Type.EXPENSE.toString()).findAll();
    }

    public Recurring getById(String id) {
        return realm.where(Recurring.class).equalTo("id", id).findFirst();
    }

    public void editCategory(Recurring recurring, Category newCategory) {
        realm.beginTransaction();
        recurring.setCategory(newCategory);
        realm.commitTransaction();
    }

    public void editAccount(Recurring recurring, Account newAccount) {
        realm.beginTransaction();
        recurring.setAccount(newAccount);
        realm.commitTransaction();
    }

    public void editStartDate(Recurring recurring, Date newStartDate) {
        realm.beginTransaction();
        recurring.setStartDate(newStartDate);
        realm.commitTransaction();
    }

    public void editNextTransactionDate(Recurring recurring, Date newNextTransactionDate) {
        realm.beginTransaction();
        recurring.setNextTransactionDate(newNextTransactionDate);
        realm.commitTransaction();
    }

    public void editAmount(Recurring recurring, BigDecimal newAmount) {
        realm.beginTransaction();
        recurring.setAmount(newAmount);
        realm.commitTransaction();
    }

    public void editMode(Recurring recurring, Recurring.Mode newMode) {
        realm.beginTransaction();
        recurring.setMode(newMode);
        realm.commitTransaction();
    }
}
