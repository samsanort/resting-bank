package com.samsanort.restingbank.model.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Bank account entity.
 */
@Entity
public class BankAccount {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private BigDecimal balance = new BigDecimal(0);

    @OneToOne
    private User owner;

    @OneToMany
    private List<AccountTransaction> transactions = new ArrayList<>();

    public BankAccount() {}

    public BankAccount(Long id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<AccountTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<AccountTransaction> transactions) {
        this.transactions = transactions;
    }

    public void setOwner(User user) { this.owner = user; }

    public User getOwner() { return this.owner; }
}
