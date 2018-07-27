package com.boyanstoynov.littlebigspender.db.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Realm model of Category Entity.
 *
 * @author Boyan Stoynov
 */
public class Category extends RealmObject {
    @PrimaryKey
    private String name;
    @Required
    private String type;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setType(Type type) {
        this.type = type.toString();
    }

    public Type getType() {
        return Type.valueOf(type);
    }

    @Override
    public String toString() {
        return name;
    }

    public enum Type {
        EXPENSE, INCOME
    }
}

