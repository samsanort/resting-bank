package com.samsanort.restingbank.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

/**
 * Created by samu on 2/8/17.
 */
public class AuthenticatedUser extends User {

    private Long id;

    /**
     * TODO describe
     * @param id
     * @param email
     * @param password
     */
    public AuthenticatedUser(Long id, String email, String password) {

        super(email, password, true, true, true, true, AuthorityUtils.createAuthorityList("USER"));

        this.id = id;
    }

    /**
     * TODO describe
     * @return
     */
    public Long getId(){ return this.id; }
}
