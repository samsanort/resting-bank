package com.samsanort.restingbank.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO add description
 */
public class StatementDto {

    private Date date;
    private BigDecimal balance;
    private List<TransactionDto> transactions = new ArrayList<>();

    /**
     * C'tor.
     * @param date
     * @param balance
     */
    public StatementDto(Date date, BigDecimal balance) {
        this.date = date;
        this.balance = balance;
    }

    public StatementDto(){}

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<TransactionDto> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDto> transactions) {
        this.transactions = transactions;
    }
}
