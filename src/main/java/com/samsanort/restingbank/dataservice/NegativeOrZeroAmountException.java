package com.samsanort.restingbank.dataservice;

import java.math.BigDecimal;

/**
 * Thrown to indicate that a zero or negative amount was provided, when a
 * positive figure was expected.
 */
public class NegativeOrZeroAmountException extends RuntimeException {

    /**
     * C'tor.
     * @param amount The amount provided.
     */
    public NegativeOrZeroAmountException(BigDecimal amount) {
        super(String.format("%s", amount));
    }
}
