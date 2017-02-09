package com.samsanort.restingbank.model.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * TODO add description
 */
@Entity
public class AccountTransaction {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Date date;
    private BigDecimal amount;
    private String description;

    public AccountTransaction(){}

    public AccountTransaction(Date date, BigDecimal amount, String description) {
        this.date = date;
        this.amount = amount;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
