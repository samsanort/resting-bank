package com.samsanort.restingbank.dataservice;

import com.samsanort.restingbank.model.dto.BankAccountDto;
import com.samsanort.restingbank.model.dto.UserDto;

import java.util.List;

/**
 * TODO add description
 */
public interface UserDataService {

    /**
     *
     * @param email
     * @param password
     * @throws EmailAlreadyRegisteredException
     * @throws IllegalArgumentException
     *  if the email has an invalid format, the password is blank or either one are null.
     */
    void register(String email, String password);

    /**
     *
     * @param userId
     * @return
     * @throws UserNotFoundException
     */
    List<BankAccountDto> getBankAccounts(Long userId);
}
