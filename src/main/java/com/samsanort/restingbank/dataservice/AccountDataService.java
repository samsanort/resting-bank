package com.samsanort.restingbank.dataservice;

import com.samsanort.restingbank.model.dto.StatementDto;

import java.math.BigDecimal;

/**
 * TODO add description
 */
public interface AccountDataService {

    /**
     *
     * @param accountId
     * @param amount
     * @throws AccountNotFoundException
     * @throws IllegalArgumentException
     * @throws InsufficientFundsException
     */
    void withdraw(String accountId, BigDecimal amount);

    /**
     *
     * @param accountId
     * @param amount
     * @throws AccountNotFoundException
     * @throws IllegalArgumentException
     */
    void deposit(String accountId, BigDecimal amount);

    /**
     *
     * @param accountId
     * @return
     * @throws AccountNotFoundException
     */
    StatementDto statement(String accountId);

    /**
     *
     * @param accountId
     * @return
     * @throws AccountNotFoundException
     */
    BigDecimal balance(String accountId);
}
