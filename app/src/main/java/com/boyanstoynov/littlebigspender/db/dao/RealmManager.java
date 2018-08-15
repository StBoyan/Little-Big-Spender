package com.boyanstoynov.littlebigspender.db.dao;

import io.realm.Realm;

/**
 * Provides abstraction over Realm instance management and creation of
 * Data Access Objects. DAOs can only be instantiated via the RealmManager
 * since they all have package private constructors. Also provides method
 * to erase the data from the database.
 *
 * @author Boyan Stoynov
 */
public class RealmManager {

    private Realm realm;

    public void open() {
        realm = Realm.getDefaultInstance();
    }

    public void close() {
        if (realm != null)
            realm.close();
    }

    public AccountDao createAccountDao() {
        checkForOpenRealm();
        return new AccountDao(realm);
    }

    public CategoryDao createCategoryDao() {
        checkForOpenRealm();
        return new CategoryDao(realm);
    }

    public TransactionDao createTransactionDao() {
        checkForOpenRealm();
        return new TransactionDao(realm);
    }

    public RecurringDao createRecurringDao() {
        checkForOpenRealm();
        return new RecurringDao(realm);
    }

    public void eraseRealm() {
        checkForOpenRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
    }

    private void checkForOpenRealm() {
        if (realm == null || realm.isClosed())
            throw new IllegalStateException("Realm is closed. Call open() first.");
    }
}
