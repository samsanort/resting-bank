package com.samsanort.restingbank.dataservice;

/**
 * TODO add description
 */
public class EmailAlreadyRegisteredException extends RuntimeException {

    /**
     * TODO describe
     * @param email
     */
    public EmailAlreadyRegisteredException(String email) {
        super("Email: " + email);
    }
}
