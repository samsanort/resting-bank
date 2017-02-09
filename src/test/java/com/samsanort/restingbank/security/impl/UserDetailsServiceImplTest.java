package com.samsanort.restingbank.security.impl;

import com.samsanort.restingbank.model.entity.User;
import com.samsanort.restingbank.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * TODO add description
 */
@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl testSubject;

    @Mock
    private UserRepository userRepository;

    @Test
    public void loadUserByUsername_userExists_returnsUserDetails() {

        // Given
        User existingUser = new User("name@email.com", "secret_password");
        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(existingUser);

        // When
        UserDetails userDetails = testSubject.loadUserByUsername(existingUser.getEmail());

        // Then
        assertThat(userDetails.getUsername(), is( equalTo( existingUser.getEmail() )));
        assertThat(userDetails.getPassword(), is( equalTo( existingUser.getPassword() )));
        assertThat(userDetails.isAccountNonExpired(), is( equalTo(true)));
        assertThat(userDetails.isAccountNonLocked(), is( equalTo(true)));
        assertThat(userDetails.isCredentialsNonExpired(), is( equalTo(true)));
        assertThat(userDetails.isEnabled(), is( equalTo(true)));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_userDoesNotExists_throwsUsernameNotFoundException() {

        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        // When
        testSubject.loadUserByUsername("any@email.com");
    }
}
