package com.boyanstoynov.littlebigspender.db.dao;

import android.support.annotation.NonNull;

import com.boyanstoynov.littlebigspender.db.model.Transaction;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Data access object that defines queries for Transaction
 * entities.
 *
 * @author Boyan Stoynov
 */
public class TransactionDao extends BaseDao<Transaction> {

    public TransactionDao(@NonNull Realm realm) {
        super(realm);
    }

    /**
     * Gets all Transaction entities stored in the database.
     * @return all Transaction from database
     */
    public RealmResults<Transaction> getAll() {
       return realm.where(Transaction.class).findAll();
    }
}
