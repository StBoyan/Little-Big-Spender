package com.boyanstoynov.littlebigspender.db.dao;

import android.support.annotation.NonNull;

import com.boyanstoynov.littlebigspender.db.model.Account;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Data access object that defines queries for Account
 * entities.
 *
 * @author Boyan Stoynov
 */
public class AccountDao extends BaseDao<Account> {

    public AccountDao(@NonNull Realm realm) {
        super(realm);
    }

    public RealmResults<Account> getAll() {
        return realm.where(Account.class).findAll();
    }
}
