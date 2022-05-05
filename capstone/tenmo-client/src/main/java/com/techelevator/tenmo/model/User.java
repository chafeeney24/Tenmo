package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class User {

    private int userId;
    @JsonProperty("username")
    private String username;
    @JsonProperty("balance")
    private BigDecimal accountBalance;


    public User() {
    }

    public User(int userId, String username, int accountId, BigDecimal accountBalance) {
        this.userId = userId;
        this.username = username;
        this.accountBalance = accountBalance;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public BigDecimal getAccountBalance() {return accountBalance;}

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public int getId() {
        return userId;
    }


    public String getUsername() {
        return username;
    }


    @Override
    public boolean equals(Object other) {
        if (other instanceof User) {
            User otherUser = (User) other;
            return otherUser.getId() == userId
                    && otherUser.getUsername().equals(username);
        } else {
            return false;
        }
    }

    public String selectionPrint () {
        return userId + " " + username;
    }
}
