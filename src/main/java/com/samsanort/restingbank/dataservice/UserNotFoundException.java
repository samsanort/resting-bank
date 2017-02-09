package com.samsanort.restingbank.dataservice;

/**
 * TODO add description
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * TODO describe
     * @param userId
     */
    public UserNotFoundException(Long userId) {
        super(String.format("User ID: %s", userId).toString());
    }
}
