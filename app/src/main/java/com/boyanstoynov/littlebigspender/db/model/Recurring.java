package com.boyanstoynov.littlebigspender.db.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Realm model of Recurring Transaction entity.
 *
 * @author Boyan Stoynov
 */
public class Recurring extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private Category category;
    private Account account;
    @Required
    private Date startDate;
    @Required
    private Date nextTransactionDate;
    private long amount;
    @Required
    private String mode;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getNextTransactionDate() {
        return nextTransactionDate;
    }

    public void setNextTransactionDate(Date nextTransactionDate) {
        this.nextTransactionDate = nextTransactionDate;
    }

    public BigDecimal getAmount() {
        return new BigDecimal(amount).divide(new BigDecimal(100));
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount.multiply(new BigDecimal(100)).longValue();
    }

    public Mode getMode() {
        return Mode.valueOf(mode);
    }

    public void setMode(Mode mode) {
        this.mode = mode.toString();
    }

    /**
     * Recurrence mode of the transaction.
     */
    public enum Mode {
        MONTHLY, BIWEEKLY, WEEKLY
    }
}
