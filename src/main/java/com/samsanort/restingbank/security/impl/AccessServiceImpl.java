package com.samsanort.restingbank.security.impl;

import com.samsanort.restingbank.dataservice.AccountDataService;
import com.samsanort.restingbank.security.AccessService;
import com.samsanort.restingbank.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * @see AccessService
 */

@Service("accessService")
public class AccessServiceImpl implements AccessService {

    @Autowired
    private AccountDataService accountDataService;

    @Override
    public boolean canManageUser(Authentication authentication, Long userId) {

        if(userId == null) {
            throw new IllegalArgumentException("Provided NULL userId.");
        }

        AuthenticatedUser authenticatedUser = getAuthenticatedUser(authentication);

        return authenticatedUser.getId().equals(userId);
    }

    @Override
    public boolean canManageAccount(Authentication authentication, Long accountId) {

        if(accountId == null) {
            throw new IllegalArgumentException("Provided NULL accountId.");
        }

        Long accountOwnerId = accountDataService.getOwnerId(accountId);

        if(accountOwnerId == null) {
            throw new RuntimeException("Bank account without owner.");
        }

        AuthenticatedUser authenticatedUser = getAuthenticatedUser(authentication);

        return accountOwnerId.equals( authenticatedUser.getId() );
    }

    private AuthenticatedUser getAuthenticatedUser(Authentication authentication) {

        if(authentication == null) {
            throw new IllegalArgumentException("Authentication is NULL.");
        }

        Object principal = authentication.getPrincipal();

        if(principal == null) {
            throw new RuntimeException("No principal found in authentication.");
        }

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        if(authenticatedUser.getId() == null) {
            throw new RuntimeException("Authenticated user has NULL id.");
        }

        return authenticatedUser;
    }
}
