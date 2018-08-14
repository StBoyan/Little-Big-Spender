package com.boyanstoynov.littlebigspender.db.model;

import java.math.BigDecimal;

import io.realm.RealmObject;

/**
 * Realm Model of cryptocurrency account data.
 *
 * @author Boyan Stoynov
 */
public class CryptoData extends RealmObject {

    private long lastUpdated;
    private long fiatValue;

    public CryptoData() {
        lastUpdated = 0;
        fiatValue = 0;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public BigDecimal getFiatValue() {
        return new BigDecimal(fiatValue).divide(new BigDecimal(100));
    }

    public void setFiatValue(BigDecimal fiatValue) {
        this.fiatValue = fiatValue.multiply(new BigDecimal(100)).longValue();
    }
}
