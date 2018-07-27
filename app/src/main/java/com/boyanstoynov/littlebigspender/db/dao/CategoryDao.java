package com.boyanstoynov.littlebigspender.db.dao;

import android.support.annotation.NonNull;

import com.boyanstoynov.littlebigspender.db.model.Category;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Data access object that defines queries for Category
 * entities.
 *
 * @author Boyan Stoynov
 */
public class CategoryDao extends BaseDao<Category> {

    public CategoryDao(@NonNull Realm realm) {
        super(realm);
    }

    public RealmResults<Category> getAllIncomeCategories() {
        return realm.where(Category.class).equalTo("type", Category.Type.INCOME.toString()).findAll();
    }

    public RealmResults<Category> getAllExpenseCategories() {
        return realm.where(Category.class).equalTo("type", Category.Type.EXPENSE.toString()).findAll();
    }
}
