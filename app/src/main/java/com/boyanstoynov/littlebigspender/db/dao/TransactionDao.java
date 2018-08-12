package com.boyanstoynov.littlebigspender.db.dao;

import android.support.annotation.NonNull;

import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Transaction;
import com.boyanstoynov.littlebigspender.util.DateTimeUtils;

import java.math.BigDecimal;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Data access object for queries and transactions on a
 * Transaction entity.
 *
 * @author Boyan Stoynov
 */
public class TransactionDao extends BaseDao<Transaction> {

    TransactionDao(@NonNull Realm realm) {
        super(realm);
    }

    public RealmResults<Transaction> getAll() {
       return realm.where(Transaction.class).findAll();
    }

    public RealmResults<Transaction> getByType(Category.Type type) {
        return realm.where(Transaction.class).equalTo("category.type", type.toString()).findAll();
    }

    public RealmResults<Transaction> getByCategory(Category category) {
       return realm.where(Transaction.class).equalTo("category.id", category.getId()).findAll();
    }

    public RealmResults<Transaction> getByAccount(Account account) {
        return realm.where(Transaction.class).equalTo("account.id", account.getId()).findAll();
    }

    public RealmResults<Transaction> getByDate(Date date) {
        return realm.where(Transaction.class).between("date",
                DateTimeUtils.startOfDay(date), DateTimeUtils.endOfDay(date)).findAll();
    }

    public Transaction getById(String id) {
        return realm.where(Transaction.class).equalTo("id", id).findFirst();
    }

    public void editCategory(Transaction transaction, Category newCategory) {
        realm.beginTransaction();
        transaction.setCategory(newCategory);
        realm.commitTransaction();
    }

    public void editAccount(Transaction transaction, Account newAccount) {
        realm.beginTransaction();
        transaction.setAccount(newAccount);
        realm.commitTransaction();
    }

    public void editDate(Transaction transaction, Date newDate) {
        realm.beginTransaction();
        transaction.setDate(newDate);
        realm.commitTransaction();
    }

    public void editAmount(Transaction transaction, BigDecimal newAmount) {
        realm.beginTransaction();
        transaction.setAmount(newAmount);
        realm.commitTransaction();
    }
}
