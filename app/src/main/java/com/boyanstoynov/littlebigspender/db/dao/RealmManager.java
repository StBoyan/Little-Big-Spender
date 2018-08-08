package com.boyanstoynov.littlebigspender.db.dao;

import io.realm.Realm;

/**
 * Provides abstraction over Realm instance management and creation of
 * Data Access Objects. DAOs can only be instantiated via the RealmManager
 * since they all have package private constructors.
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

    private void checkForOpenRealm() {
        if (realm == null || realm.isClosed())
            throw new IllegalStateException("Realm is closed. Call open() first.");
    }
}
