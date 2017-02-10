package com.samsanort.restingbank.model.dto;

import java.math.BigDecimal;

/**
 * DTO info holder for a Bank account.
 */
public class BankAccountDto {

    private Long id;
    private BigDecimal balance;

    public BankAccountDto(){}

    public BankAccountDto(Long id, BigDecimal balance) {
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
}
