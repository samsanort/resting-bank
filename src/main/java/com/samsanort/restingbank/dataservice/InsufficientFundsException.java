package com.samsanort.restingbank.dataservice;

import java.math.BigDecimal;

/**
 * Thrown to indicate that a withdrawal operation was attempted on a bank account
 * which didn't have enough funds to do so.
 */
public class InsufficientFundsException extends RuntimeException {

    /**
     * C'tor.
     * @param accountBalance The account balance at the time of the withdrawal attempt.
     * @param attemptedWithdrawal The attempted withdrawal amount.
     */
    public InsufficientFundsException(BigDecimal accountBalance, BigDecimal attemptedWithdrawal) {
        super( String.format(
                "Attempted to withdraw %s, only %s available.",
                accountBalance,
                attemptedWithdrawal)
                .toString() );
    }
}
