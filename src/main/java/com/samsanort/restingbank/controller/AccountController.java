package com.samsanort.restingbank.controller;

import com.samsanort.restingbank.model.dto.StatementDto;

import java.math.BigDecimal;

/**
 * Bank account operations.
 */
public interface AccountController {

    /**
     * Withdraws from the account.
     * @param accountId The account to withdraw from.
     * @param amount The amount to be withdrawn.
     */
    void withdraw(Long accountId, BigDecimal amount);

    /**
     * Deposits into the account.
     * @param accountId The account to deposit into.
     * @param amount The amount to deposit.
     */
    void deposit(Long accountId, BigDecimal amount);

    /**
     * Obtains the current account statement.
     * @param accountId The account to obtain the statement for.
     * @return The statement info.
     */
    StatementDto statement(Long accountId);
}
