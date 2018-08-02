package com.boyanstoynov.littlebigspender.db.dao;

import android.support.annotation.NonNull;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Base Data Access Object that serves to abstract database
 * operations on entities stored in the Realm database. It provides
 * implementation for storing an instance of the database to work with
 * and for saving and deleting RealmObjects to/from the database. It also
 * provides a mean to get an unmanaged copy of a managed RealmObject.
 *
 * Subclasses need to call the super constructor and implement queries and
 * edit transactions for their particular model.
 *
 * @author Boyan Stoynov
 */
public abstract class BaseDao<T extends RealmObject> {

    protected final Realm realm;

    /**
     * Constructor that takes a Realm instance on which to execute
     * transactions and queries.
     * @param realm Realm instance
     */
    BaseDao(@NonNull Realm realm) {
        this.realm = realm;
    }

    /**
     * Save a RealmObject to the database.
     * @param realmObject RealmObject to save
     */
    public void save(final T realmObject) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.copyToRealm(realmObject);
            }
        });
    }

    /**
     * Delete the RealmObject from the database.
     * @param realmObject RealmObject to delete
     */
    public void delete(final T realmObject) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realmObject.deleteFromRealm();
            }
        });
    }

    /**
     * Get an unmanaged copy of the RealmObject.
     *
     * Note: Unmanaged RealmObjects are not persisted in the Realm
     * and can be edited outside of a transaction.
     * @param realmObject managed (live) RealmObject
     * @return unmanaged RealmObject
     */
    public T getUnmanaged(T realmObject) {
        return realm.copyFromRealm(realmObject);
    }
}
