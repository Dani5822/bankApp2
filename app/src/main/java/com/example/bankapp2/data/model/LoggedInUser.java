package com.example.bankapp2.data.model;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private final String id;
    private String firstName;
    private String lastName;
    private String role;
    private String email;
    private String access_token;
    private Date createdAt;
    private Date updatedAt;
    private Card[] Accounts;
    private Expense[] expenseId;
    private Income[] incomeId;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public LoggedInUser(String id, String firstName, String lastName, String role, String email, String access_token, Date createdAt, Date updatedAt, Card[] Accounts, Expense[] expenses, Income[] incomes) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.email = email;
        this.access_token = access_token;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.Accounts = Accounts;
        this.expenseId = expenses;
        this.incomeId = incomes;

    }


    public String getId() {
        return id;
    }

    public Date getUpdatedat() {
        return updatedAt;
    }

    public void setUpdatedat(Date updatedat) {
        this.updatedAt = updatedat;
    }

    public Date getCreatedat() {
        return createdAt;
    }

    public void setCreatedat(Date createdat) {
        this.createdAt = createdat;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLastname() {
        return lastName;
    }

    public void setLastname(String lastname) {
        this.lastName = lastname;
    }

    public String getFirstname() {
        return firstName;
    }

    public void setFirstname(String firstname) {
        this.firstName = firstname;
    }

    public Card[] getCards() {
        return Accounts;
    }

    public void setCards(Card[] cards) {
        this.Accounts = cards;
    }

    public Expense[] getExpenses() {
        return expenseId;
    }

    public void setExpenses(Expense[] expenses) {
        this.expenseId = expenses;
    }

    public Income[] getIncomes() {
        return incomeId;
    }

    public void setIncomes(Income[] incomes) {
        this.incomeId = incomes;
    }

    @Override
    public String toString() {
        return "LoggedInUser{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", access_token='" + access_token + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", Accounts=" + Arrays.toString(Accounts) +
                ", expenseId=" + Arrays.toString(expenseId) +
                ", incomeId=" + Arrays.toString(incomeId) +
                ", format=" + format +
                '}';
    }
}