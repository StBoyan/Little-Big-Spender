package com.boyanstoynov.littlebigspender.db.model;

import java.math.BigDecimal;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Realm Model of Account entity.
 *
 * @author Boyan Stoynov
 */
public class Account extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    @Required
    private String name;
    private long balance;
    private CryptoData cryptoData;

    public String getId() {
        return this.id;
    }

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

    public boolean isCrypto() {
        return cryptoData != null;
    }

    public long getLastUpdated() {
        return cryptoData.getLastUpdated();
    }

    public void setLastUpdated(long lastUpdated) {
        cryptoData.setLastUpdated(lastUpdated);
    }

    public BigDecimal getFiatValue() {
        return cryptoData.getFiatValue();
    }

    public void setFiatValue(BigDecimal fiatValue) {
        cryptoData.setFiatValue(fiatValue);
    }

    @Override
    public String toString() {
        return name;
    }
}
