package com.samsanort.restingbank.dataservice;

/**
 * Thrown to indicate that occurred a reference to a user id
 * that does not exist.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * C'tor.
     * @param userId The unexisting referenced user.
     */
    public UserNotFoundException(Long userId) {
        super(String.format("User ID: %s", userId).toString());
    }
}
