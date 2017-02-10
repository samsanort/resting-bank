package com.samsanort.restingbank.security;

import org.springframework.security.core.Authentication;

/**
 * Access control service to perform authenticated user / resource validations.
 */

public interface AccessService {

    /**
     * Checks if the authenticated user can manage the provided user.
     * @param authentication The authentication info.
     * @param userId The user to be managed.
     * @return TRUE if the user can be managed by the authenticated user, FALSE otherwise.
     */
    boolean canManageUser(Authentication authentication, Long userId);

    /**
     * Checks if the authenticated user can manage the provided account.
     * @param authentication The authentication info.
     * @param accountId The account to be managed.
     * @return TRUE if the account can be managed by the authenticated user, FALSE otherwise.
     */
    boolean canManageAccount(Authentication authentication, Long accountId);
}
