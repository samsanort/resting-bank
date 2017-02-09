package com.samsanort.restingbank.dataservice.impl;

import com.samsanort.restingbank.dataservice.EmailAlreadyRegisteredException;
import com.samsanort.restingbank.dataservice.UserDataService;
import com.samsanort.restingbank.dataservice.UserNotFoundException;
import com.samsanort.restingbank.model.business.EmailAddress;
import com.samsanort.restingbank.model.dto.BankAccountDto;
import com.samsanort.restingbank.model.dto.UserDto;
import com.samsanort.restingbank.model.entity.BankAccount;
import com.samsanort.restingbank.model.entity.User;
import com.samsanort.restingbank.repository.BankAccountRepository;
import com.samsanort.restingbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO describe
 */

@Component("userDataService")
public class UserDataServiceImpl implements UserDataService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Override
    public void register(String email, String password) {

        validateEmailAndPassword( email, password);

        User user = userRepository.findByEmail(email);

        if(user != null) {
            throw new EmailAlreadyRegisteredException(email);
        }

        // Save entities

        user = userRepository.save(new User(email, password));
        BankAccount account = bankAccountRepository.save(new BankAccount());

        // Save entity relationships

        user.setAccount( account );
        userRepository.save( user );

        account.setOwner(user);
        bankAccountRepository.save(account);
    }

    @Override
    public List<BankAccountDto> getBankAccounts(Long userId) {

        User user = userRepository.findOne(userId);

        if(user == null) {
            throw new UserNotFoundException(userId);
        }

        List<BankAccountDto> accounts = new ArrayList<>();
        if(user.getAccount() != null) {

            accounts.add(
                    new BankAccountDto(
                            user.getAccount().getId(),
                            user.getAccount().getBalance()) );
        }

        return accounts;
    }

    private void validateEmailAndPassword(String email, String password) {

        if( email == null) throw new IllegalArgumentException("Null email.");
        if( password == null) throw new IllegalArgumentException("Null password.");
        if( password.isEmpty()) throw new IllegalArgumentException("Empty password.");
        EmailAddress emailAddress = new EmailAddress(email);
    }
}
