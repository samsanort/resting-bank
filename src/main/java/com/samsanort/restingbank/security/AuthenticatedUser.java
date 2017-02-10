package com.samsanort.restingbank.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

/**
 * Custom authenticated user info, to hold extra info.
 */
public class AuthenticatedUser extends User {

    private Long id;

    /**
     * C'tor.
     * @param id The user Id.
     * @param email The user email.
     * @param password The user password.
     */
    public AuthenticatedUser(Long id, String email, String password) {

        super(email, password, true, true, true, true, AuthorityUtils.createAuthorityList("USER"));

        this.id = id;
    }

    /**
     * Gets the id of the authenticated user.
     * @return The authenticated user id.
     */
    public Long getId(){ return this.id; }
}
