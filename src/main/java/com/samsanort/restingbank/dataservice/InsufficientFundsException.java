package com.samsanort.restingbank.dataservice;

import java.math.BigDecimal;

/**
 * TODO add description
 */
public class InsufficientFundsException extends RuntimeException {

    /**
     * TODO describe
     * @param accountBalance
     * @param attemptedWithdrawal
     */
    public InsufficientFundsException(BigDecimal accountBalance, BigDecimal attemptedWithdrawal) {
        super( String.format(
                "Attempted to withdraw %s, only %s available.",
                accountBalance,
                attemptedWithdrawal)
                .toString() );
    }
}
