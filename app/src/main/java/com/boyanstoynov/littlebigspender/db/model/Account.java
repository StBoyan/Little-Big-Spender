package com.boyanstoynov.littlebigspender.db.model;

import java.math.BigDecimal;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
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
    private static final int CRYPTO_PRECISION_MULTIPLIER = 100000000;
    private static final int FIAT_PRECISION_MULTIPLIER = 100;

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
        if (isCrypto())
            return new BigDecimal(balance).divide(new BigDecimal(CRYPTO_PRECISION_MULTIPLIER));
        else
            return new BigDecimal(balance).divide(new BigDecimal(FIAT_PRECISION_MULTIPLIER));
    }

    public void setBalance(BigDecimal balance) {
        if (isCrypto())
            this.balance = balance.multiply(new BigDecimal(CRYPTO_PRECISION_MULTIPLIER)).longValue();
        else
            this.balance = balance.multiply(new BigDecimal(FIAT_PRECISION_MULTIPLIER)).longValue();
    }

    public void setCryptoData(CryptoData cryptoData) {
        this.cryptoData = cryptoData;
    }

    public boolean isCrypto() {
        return cryptoData != null;
    }

    public long getLastUpdated() {
        return (cryptoData != null) ? cryptoData.getLastUpdated() : -1;
    }

    public void setLastUpdated(long lastUpdated) {
        if (cryptoData != null)
            cryptoData.setLastUpdated(lastUpdated);
    }

    public BigDecimal getFiatValue() {
        return (cryptoData != null) ? cryptoData.getFiatValue() : null;
    }

    public void setFiatValue(BigDecimal fiatValue) {
        if (cryptoData != null)
            cryptoData.setFiatValue(fiatValue);
    }

    @Override
    public String toString() {
        return name;
    }
}
