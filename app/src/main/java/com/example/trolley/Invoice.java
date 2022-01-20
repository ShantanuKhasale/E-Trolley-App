package com.example.trolley;

public class Invoice {
    private int id, userId;
    private String date;
    private boolean paid;

    public Invoice(int id, int userId, String date, boolean paid) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.paid = paid;
    }

    public int getId() { return id; }

    public int getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public boolean getPaid(){ return paid; }
}

