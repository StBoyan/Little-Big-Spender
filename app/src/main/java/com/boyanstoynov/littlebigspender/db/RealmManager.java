package com.boyanstoynov.littlebigspender.db;

import com.boyanstoynov.littlebigspender.db.dao.AccountDao;
import com.boyanstoynov.littlebigspender.db.dao.CategoryDao;

import io.realm.Realm;

/**
 * Provides abstraction over Realm instance management
 * and creation of Data Access Objects.
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

    private void checkForOpenRealm() {
        if (realm == null || realm.isClosed())
            throw new IllegalStateException("Realm is closed. Call open() first.");
    }
}
