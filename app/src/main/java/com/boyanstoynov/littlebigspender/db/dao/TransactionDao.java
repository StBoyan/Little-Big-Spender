package com.boyanstoynov.littlebigspender.db.dao;

import android.support.annotation.NonNull;

import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Transaction;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

    public RealmResults<Transaction> getByType(Category.Type type) {
        return realm.where(Transaction.class).equalTo("category.type", type.toString()).findAll();
    }
    //TODO change to use ID's instead of name for category and account
    public RealmResults<Transaction> getByCategory(Category category) {
       return realm.where(Transaction.class).equalTo("category.name", category.getName()).findAll();
    }

    public RealmResults<Transaction> getByAccount(Account account) {
        return realm.where(Transaction.class).equalTo("account.name", account.getName()).findAll();
    }

    public RealmResults<Transaction> getByDate(Date date) {
        //TODO move this functionality to date util
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date1 = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        Date date2 = calendar.getTime();
        return realm.where(Transaction.class).between("date", date1, date2).findAll();
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
