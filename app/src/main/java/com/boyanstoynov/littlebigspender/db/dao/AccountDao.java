package com.boyanstoynov.littlebigspender.db.dao;

import android.support.annotation.NonNull;

import com.boyanstoynov.littlebigspender.db.model.Account;

import java.math.BigDecimal;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Data access object for queries and transactions on an
 * Account entity.
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

    public Account getById(String id) {
        return realm.where(Account.class).equalTo("id", id).findFirst();
    }

    public void addAmount(Account toAccount, BigDecimal amount) {
        realm.beginTransaction();
        toAccount.setBalance(toAccount.getBalance().add(amount));
        realm.commitTransaction();
    }

    public void subtractAmount(Account fromAccount, BigDecimal amount) {
        realm.beginTransaction();
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        realm.commitTransaction();
    }

    public void editBalance(Account account, BigDecimal newBalance) {
        realm.beginTransaction();
        account.setBalance(newBalance);
        realm.commitTransaction();
    }

    public void editName(Account account, String newName) {
        realm.beginTransaction();
        account.setName(newName);
        realm.commitTransaction();
    }
}
