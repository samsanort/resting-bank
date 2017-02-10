package com.samsanort.restingbank.security.impl;

import com.samsanort.restingbank.model.entity.User;
import com.samsanort.restingbank.repository.UserRepository;
import com.samsanort.restingbank.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @see UserDetailsService
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);

        if(user == null) {
            throw new UsernameNotFoundException("User with email '" + email + "' not found.");
        }

        return new AuthenticatedUser(user.getId(), user.getEmail(), user.getPassword());
    }
}
