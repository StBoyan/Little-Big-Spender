package com.boyanstoynov.littlebigspender.db.dao;

import android.support.annotation.NonNull;

import com.boyanstoynov.littlebigspender.db.model.Account;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Data access object for Account entity.
 *
 * @author Boyan Stoynov
 */
public class AccountDao {

    private Realm realm;

    public AccountDao(@NonNull Realm realm) {
        this.realm = realm;
    }

    public void save(final Account account) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(account);
            }
        });
    }

    public RealmResults<Account> getAll() {
        return realm.where(Account.class).findAll();
    }

    public void deleteByName(String name) {
        final Account account = realm.where(Account.class).equalTo("name", name).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                account.deleteFromRealm();
            }
        });
    }
}
