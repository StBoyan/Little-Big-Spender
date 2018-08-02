package com.boyanstoynov.littlebigspender.db.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Realm model of Transaction entity.
 *
 * @author Boyan Stoynov
 */
public class Transaction extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private Category category;
    private Account account;
    @Required
    private Date date;
    private long amount;

    public String getId() {
        return this.id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return new BigDecimal(amount).divide(new BigDecimal(100));
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount.multiply(new BigDecimal(100)).longValue();
    }
}
