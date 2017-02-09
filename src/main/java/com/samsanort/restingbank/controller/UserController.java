package com.samsanort.restingbank.controller;

import com.samsanort.restingbank.model.dto.BankAccountDto;
import com.samsanort.restingbank.model.dto.UserDto;

import java.util.List;

/**
 * TODO add description
 */
public interface UserController {

    /**
     *
     * @param user
     */
    void register(UserDto user);

    /**
     *
     * @param userId
     * @return
     */
    List<BankAccountDto> getBankAccounts(Long userId);
}
