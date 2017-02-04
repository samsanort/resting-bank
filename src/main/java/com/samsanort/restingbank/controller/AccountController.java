package com.samsanort.restingbank.controller;

import com.samsanort.restingbank.model.dto.StatementDto;

import java.math.BigDecimal;

/**
 * TODO add description
 */
public interface AccountController {

    /**
     *
     * @param accountId
     * @param amount
     */
    void withdraw(String accountId, BigDecimal amount);

    /**
     *
     * @param accountId
     * @param amount
     */
    void deposit(String accountId, BigDecimal amount);

    /**
     *
     * @param accountId
     * @return
     */
    StatementDto statement(String accountId);
}
