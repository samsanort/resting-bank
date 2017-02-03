package com.samsanort.restingbank.dataservice;

import com.samsanort.restingbank.model.dto.StatementDTO;

import java.math.BigDecimal;

/**
 * TODO add description
 */
public interface AccountDataService {

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
    StatementDTO statement(String accountId);

    /**
     *
     * @param accountId
     * @return
     */
    BigDecimal balance(String accountId);
}
