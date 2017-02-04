package com.samsanort.restingbank.dataservice;

/**
 * TODO add description
 */
public class AccountNotFoundException extends RuntimeException {

    /**
     * TODO describe
     * @param accountId
     */
    public AccountNotFoundException(Long accountId) {
        super(String.format("Account ID: %s", accountId).toString());
    }
}
