package com.boyanstoynov.littlebigspender.db.dao;

import android.support.annotation.NonNull;

import com.boyanstoynov.littlebigspender.db.model.Recurring;

import io.realm.Realm;

/**
 * Data access object that defines queries for Recurring
 * Transaction entities.
 *
 * @author Boyan Stoynov
 */
public class RecurringDao extends BaseDao<Recurring> {

    public RecurringDao(@NonNull Realm realm) {
        super(realm);
    }
}
