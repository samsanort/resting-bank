package com.samsanort.restingbank.dataservice;

import com.samsanort.restingbank.model.dto.StatementDto;

import java.math.BigDecimal;

/**
 * Data service for bank account operations.
 */
public interface AccountDataService {

    /**
     * Withdraws from the account.
     * @param accountId The account to withdraw from.
     * @param amount The amount to be withdrawn.
     * @throws AccountNotFoundException If the provided account does not exist.
     * @throws InsufficientFundsException If the account does not have enough funds for the withdrawal.
     * @throws NegativeOrZeroAmountException If the provided amount is not a positive figure.
     */
    void withdraw(Long accountId, BigDecimal amount);

    /**
     * Deposits into the account.
     * @param accountId The account to deposit into.
     * @param amount The amount to be deposited.
     * @throws AccountNotFoundException If the provided account does not exist.
     * @throws InsufficientFundsException If the account does not have enough funds for the withdrawal.
     * @throws NegativeOrZeroAmountException If the provided amount is not a positive figure.
     */
    void deposit(Long accountId, BigDecimal amount);

    /**
     * Obtains the current account statement.
     * @param accountId The account to obtain the statement for.
     * @return The current account statement.
     * @throws AccountNotFoundException If the provided account does not exist.
     */
    StatementDto statement(Long accountId);

    /**
     * Obtains the id of the owner of the provided account.
     * @param accountId The account to obtain the owner for.
     * @return The id of the account owner.
     */
    Long getOwnerId(Long accountId);
}
