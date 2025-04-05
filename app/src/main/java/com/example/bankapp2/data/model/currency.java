package com.example.bankapp2.data.model;

import java.util.Date;
import java.util.Map;

public class currency {
    private Date date;
    private Map<String, Double> eur;
    private String name;


    public currency(Date date, Map<String, Double> eur) {
        this.date = date;
        this.eur = eur;
    }

    public currency() {

    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<String, Double> getEur() {
        return eur;
    }

    public void setEur(Map<String, Double> eur) {
        this.eur = eur;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "currency{" +
                "date=" + date +
                ", eur=" + eur +
                ", name='" + name + '\'' +
                '}';
    }
}

