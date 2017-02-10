package com.samsanort.restingbank.dataservice;

import com.samsanort.restingbank.model.dto.RegisteredUserDto;

/**
 * Data service for user operations.
 */
public interface UserDataService {

    /**
     * Registers a new user identified with the provided email/password.
     * @param email The email (username) of the user to register.
     * @param password The password of the user to register.
     * @returns registeredUser The resulting registered user info.
     * @throws EmailAlreadyRegisteredException
     *  If the email is already registered.
     * @throws IllegalArgumentException
     *  if the email has an invalid format, the password is blank or either one are null.
     */
    RegisteredUserDto register(String email, String password);
}
