package com.samsanort.restingbank.controller;

import com.samsanort.restingbank.model.dto.RegisteredUserDto;
import com.samsanort.restingbank.model.dto.UserRegistrationDto;

/**
 * User operations.
 */
public interface UserController {

    /**
     * Registers a new user.
     * @param user The user to register.
     */
    RegisteredUserDto register(UserRegistrationDto user);
}
