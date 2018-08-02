package com.boyanstoynov.littlebigspender.db.dao;

import android.support.annotation.NonNull;

import com.boyanstoynov.littlebigspender.db.model.Category;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Data access object for queries and transactions on a
 * Category entity.
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

    public Category getById(String id) {
        return realm.where(Category.class).equalTo("id", id).findFirst();
    }

    public void editName(Category category, String newName) {
        realm.beginTransaction();
        category.setName(newName);
        realm.commitTransaction();
    }
}
