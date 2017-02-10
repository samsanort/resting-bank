package com.samsanort.restingbank.dataservice.impl;

import com.samsanort.restingbank.dataservice.EmailAlreadyRegisteredException;
import com.samsanort.restingbank.dataservice.UserDataService;
import com.samsanort.restingbank.model.business.EmailAddress;
import com.samsanort.restingbank.model.dto.RegisteredUserDto;
import com.samsanort.restingbank.model.entity.BankAccount;
import com.samsanort.restingbank.model.entity.User;
import com.samsanort.restingbank.repository.BankAccountRepository;
import com.samsanort.restingbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("userDataService")
public class UserDataServiceImpl implements UserDataService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Override
    public RegisteredUserDto register(String email, String password) {

        validateEmailAndPassword( email, password);

        User persistedUser = userRepository.save(new User(email, password));
        BankAccount persistedAccount = bankAccountRepository.save(new BankAccount());

        persistedUser.setAccount( persistedAccount );
        userRepository.save( persistedUser );

        persistedAccount.setOwner(persistedUser);
        bankAccountRepository.save(persistedAccount);

        return new RegisteredUserDto(persistedUser.getId(), persistedAccount.getId());
    }

    private void validateEmailAndPassword(String email, String password) {

        if( email == null) throw new IllegalArgumentException("Null email.");
        if( password == null) throw new IllegalArgumentException("Null password.");
        if( password.isEmpty()) throw new IllegalArgumentException("Empty password.");

        EmailAddress emailAddress = new EmailAddress(email);

        if(userRepository.findByEmail(emailAddress.getValue()) != null) {
            throw new EmailAlreadyRegisteredException(email);
        }
    }
}
