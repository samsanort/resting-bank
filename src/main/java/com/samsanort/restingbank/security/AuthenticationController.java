package com.samsanort.restingbank.security;

import java.security.Principal;

/**
 * TODO describe
 */
public interface AuthenticationController {

    /**
     * TODO describe
     * @param user
     * @return
     */
    Long getId(Principal user);
}
