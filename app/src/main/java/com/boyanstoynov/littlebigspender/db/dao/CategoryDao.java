package com.boyanstoynov.littlebigspender.db.dao;

import com.boyanstoynov.littlebigspender.db.model.Category;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Data access object to abstract database operations
 * on Category entities.
 *
 * @author Boyan Stoynov
 */
public class CategoryDao {

    private Realm realm;

    public CategoryDao(Realm realm) {
        this.realm = realm;
    }

    public void save(final Category category) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(category);
            }
        });
    }

    public RealmResults<Category> getAllIncomeCategories() {
        return realm.where(Category.class).equalTo("type", Category.Type.INCOME.toString()).findAll();
    }

    public RealmResults<Category> getAllExpenseCategories() {
        return realm.where(Category.class).equalTo("type", Category.Type.EXPENSE.toString()).findAll();
    }

    public void deleteByName(String name) {
        final Category category = realm.where(Category.class).equalTo("name", name).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                category.deleteFromRealm();
            }
        });
    }
}
