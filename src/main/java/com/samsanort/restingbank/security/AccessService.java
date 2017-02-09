package com.samsanort.restingbank.security;

import org.springframework.security.core.Authentication;

/**
 * TODO describe
 */

public interface AccessService {

    /**
     * TODO describe
     * @param authentication
     * @param userId
     * @return
     */
    boolean canManageUser(Authentication authentication, Long userId);

    /**
     * TODO describe
     * @param authentication
     * @param accountId
     * @return
     */
    boolean canManageAccount(Authentication authentication, Long accountId);
}
