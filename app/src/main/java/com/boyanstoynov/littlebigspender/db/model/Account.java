package com.boyanstoynov.littlebigspender.db.model;

import java.math.BigDecimal;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Realm Model of Account entity.
 *
 * @author Boyan Stoynov
 */
public class Account extends RealmObject {
    @PrimaryKey
    private String name;
    private long balance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return new BigDecimal(balance).divide(new BigDecimal(100));
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance.multiply(new BigDecimal(100)).longValue();
    }

    @Override
    public String toString() {
        return name;
    }
}
