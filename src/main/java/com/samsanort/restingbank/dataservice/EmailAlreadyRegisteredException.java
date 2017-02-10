package com.samsanort.restingbank.dataservice;

/**
 * Thrown to indicate that an occured an attempt to register an email
 * which was already registered.
 */
public class EmailAlreadyRegisteredException extends RuntimeException {

    /**
     * C'tor.
     * @param email The email that was tried to be registered.
     */
    public EmailAlreadyRegisteredException(String email) {
        super("Email: " + email);
    }
}
