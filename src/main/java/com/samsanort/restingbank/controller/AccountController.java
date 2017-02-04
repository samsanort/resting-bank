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
    void withdraw(Long accountId, BigDecimal amount);

    /**
     *
     * @param accountId
     * @param amount
     */
    void deposit(Long accountId, BigDecimal amount);

    /**
     *
     * @param accountId
     * @return
     */
    StatementDto statement(Long accountId);
}
