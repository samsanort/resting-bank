package com.samsanort.restingbank.model.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * TODO add description
 */
public class TransactionDto {

    private Date date;
    private BigDecimal amount;
    private String description;

    public TransactionDto(){}

    public TransactionDto(Date date, BigDecimal amount, String description) {
        this.date = date;
        this.amount = amount;
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
