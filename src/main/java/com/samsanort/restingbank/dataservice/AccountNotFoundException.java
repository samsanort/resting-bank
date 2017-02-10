package com.samsanort.restingbank.dataservice;

/**
 * Thrown to indicate that an operation on an unexisting bank account has been
 * attempted.
 */
public class AccountNotFoundException extends RuntimeException {

    /**
     * C'tor.
     * @param accountId The id of the account that was tried to operate with.
     */
    public AccountNotFoundException(Long accountId) {
        super(String.format("Account ID: %s", accountId).toString());
    }
}
