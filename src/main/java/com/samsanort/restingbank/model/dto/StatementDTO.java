package com.samsanort.restingbank.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DTO info holder for a statement.
 */
public class StatementDto {

    private Date date;
    private BigDecimal balance;
    private List<TransactionDto> transactions = new ArrayList<>();

    public StatementDto(){}

    public StatementDto(Date date, BigDecimal balance) {
        this.date = date;
        this.balance = balance;
    }

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
