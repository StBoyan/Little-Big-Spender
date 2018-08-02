package com.boyanstoynov.littlebigspender.db.dao;

import android.support.annotation.NonNull;

import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Transaction;

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

    public TransactionDao(@NonNull Realm realm) {
        super(realm);
    }

    public RealmResults<Transaction> getAll() {
       return realm.where(Transaction.class).findAll();
    }

    public RealmResults<Transaction> getByCategory(Category category) {
       return realm.where(Transaction.class).equalTo("category.name", category.getName()).findAll();
    }

    public RealmResults<Transaction> getByAccount(Account account) {
        return realm.where(Transaction.class).equalTo("account.name", account.getName()).findAll();
    }

    public RealmResults<Transaction> getByDate(Date date) {
        return realm.where(Transaction.class).equalTo("date", date).findAll();
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
