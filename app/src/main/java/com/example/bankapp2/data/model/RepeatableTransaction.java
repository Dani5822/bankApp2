package com.example.bankapp2.data.model;

import java.util.Date;

public class RepeatableTransaction {
    private String id;
    private String name;
    private String accountId;
    private String description;
    private double total;
    private String category;
    private Date repeatStart;
    private String repeatMetric;
    private int repeatAmount;
    private Date repeatEnd;
    private  Date lastChange;
    private  Card Account;
    private Expense[] Expenses;
    private  Date createdAt;

    public RepeatableTransaction(String id, String name ,String accountId, String description, double total, String category, Date repeatStart, String repeatMetric, int repeatAmount, Date repeatEnd, Date lastChange, Card account, Expense[] expenses, Date createdAt) {
        this.id = id;
        this.accountId = accountId;
        this.description = description;
        this.total = total;
        this.category = category;
        this.repeatStart = repeatStart;
        this.repeatMetric = repeatMetric;
        this.repeatAmount = repeatAmount;
        this.repeatEnd = repeatEnd;
        this.lastChange = lastChange;
        Account = account;
        this.createdAt = createdAt;
        Expenses = expenses;
        this.name = name;
    }

    public RepeatableTransaction(String id,String name ,String accountId, String description, double total, String category, Date repeatStart, String repeatMetric, int repeatAmount, Date repeatEnd, Date lastChange, Card account, Date createdAt) {
        this.id = id;
        this.accountId = accountId;
        this.description = description;
        this.total = total;
        this.category = category;
        this.repeatStart = repeatStart;
        this.repeatMetric = repeatMetric;
        this.repeatAmount = repeatAmount;
        this.repeatEnd = repeatEnd;
        this.lastChange = lastChange;
        Account = account;
        this.createdAt = createdAt;
        Expenses = null;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getRepeatStart() {
        return repeatStart;
    }

    public void setRepeatStart(Date repeatStart) {
        this.repeatStart = repeatStart;
    }

    public String getRepeatMetric() {
        return repeatMetric;
    }

    public void setRepeatMetric(String repeatMetric) {
        this.repeatMetric = repeatMetric;
    }

    public int getRepeatAmount() {
        return repeatAmount;
    }

    public void setRepeatAmount(int repeatAmount) {
        this.repeatAmount = repeatAmount;
    }

    public Date getRepeatEnd() {
        return repeatEnd;
    }

    public void setRepeatEnd(Date repeatEnd) {
        this.repeatEnd = repeatEnd;
    }

    public Date getLastChange() {
        return lastChange;
    }

    public void setLastChange(Date lastChange) {
        this.lastChange = lastChange;
    }

    public Card getAccount() {
        return Account;
    }

    public void setAccount(Card account) {
        Account = account;
    }

    public Expense[] getExpenses() {
        return Expenses;
    }

    public void setExpenses(Expense[] expenses) {
        Expenses = expenses;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}