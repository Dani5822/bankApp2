package com.example.bankapp2.data.model;



import java.util.Date;

public class Expense extends Transaction {


    public Expense(String id, Date createdAt, String category, String description, float total, String userId, String accountId, int repeatAmmount, String repeateMetric, Date repeateStart, Date repeatEnd, Date updatedAt, Date lastChange, String repeatableTransactionId) {
        super(id, createdAt, category, description, total, userId, accountId, repeatAmmount, repeateMetric, repeateStart, repeatEnd, updatedAt, lastChange, repeatableTransactionId);
    }
}
