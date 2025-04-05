package com.example.bankapp2.data.model;

import java.util.Date;

public class Transaction {
    private final String id;
    private String category;
    private String description;
    private double total;
    private String userId;
    private String accountId;
    private int repeatAmmount;
    private String repeateMetric;
    private Date repeateStart;
    private Date repeatEnd;
    private final Date createdAt;
    private Date updatedAt;
    private Date lastChange;
    private  String repeatableTransactionId;

    public Transaction(String id, Date createdAt, String category, String description, float total, String userId, String accountId, int repeatAmmount, String repeateMetric, Date repeateStart, Date repeatEnd, Date updatedAt,Date lastChange,String repeatableTransactionId) {
        this.id = id;
        this.createdAt = createdAt;
        this.category = category;
        this.description = description;
        this.total = total;
        this.userId = userId;
        this.accountId = accountId;
        this.repeatAmmount = repeatAmmount;
        this.repeateMetric = repeateMetric;
        this.repeateStart = repeateStart;
        this.repeatEnd = repeatEnd;
        this.updatedAt = updatedAt;
        this.lastChange= lastChange;
        this.repeatableTransactionId=repeatableTransactionId;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getRepeatAmmount() {
        return repeatAmmount;
    }

    public void setRepeatAmmount(int repeatAmmount) {
        this.repeatAmmount = repeatAmmount;
    }

    public String getRepeateMetric() {
        return repeateMetric;
    }

    public void setRepeateMetric(String repeateMetric) {
        this.repeateMetric = repeateMetric;
    }

    public Date getRepeateStart() {
        return repeateStart;
    }

    public void setRepeateStart(Date repeateStart) {
        this.repeateStart = repeateStart;
    }

    public Date getRepeatEnd() {
        return repeatEnd;
    }

    public void setRepeatEnd(Date repeatEnd) {
        this.repeatEnd = repeatEnd;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getLastChange() {
        return lastChange;
    }

    public void setLastChange(Date lastChange) {
        this.lastChange = lastChange;
    }

    public String getRepeatableTransactionId() {
        return repeatableTransactionId;
    }

    public void setRepeatableTransactionId(String repeatableTransactionId) {
        this.repeatableTransactionId = repeatableTransactionId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", total=" + total +
                ", userId='" + userId + '\'' +
                ", accountId='" + accountId + '\'' +
                ", repeatAmmount=" + repeatAmmount +
                ", repeateMetric='" + repeateMetric + '\'' +
                ", repeateStart=" + repeateStart +
                ", repeatEnd=" + repeatEnd +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", lastChange=" + lastChange +
                ", repeatableTransactionId=" + repeatableTransactionId +
                '}';
    }
}
