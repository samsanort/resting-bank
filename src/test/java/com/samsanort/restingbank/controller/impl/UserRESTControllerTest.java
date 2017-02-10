package com.samsanort.restingbank.controller.impl;

import com.samsanort.restingbank.dataservice.EmailAlreadyRegisteredException;
import com.samsanort.restingbank.dataservice.UserDataService;
import com.samsanort.restingbank.model.dto.RegisteredUserDto;
import com.samsanort.restingbank.model.dto.UserRegistrationDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @see UserRESTController
 */
@RunWith(MockitoJUnitRunner.class)
public class UserRESTControllerTest {

    @InjectMocks
    private UserRESTController testSubject;

    @Mock
    private UserDataService userDataService;

    private static final String USER_EMAIL = "user@email.com";
    private static final String USER_PASSWORD = "secret_password";

    // --- register -----------------------------------------------------------

    @Test
    public void register_happyPath_createsUserWithAccount() throws Exception {

        // Given

        UserRegistrationDto userRegistrationDto = aUserRegistration();

        when(
                userDataService.register(
                        eq(userRegistrationDto.getEmail()),
                        eq(userRegistrationDto.getPassword()))
        ).thenReturn(aRegisteredUser());

        // When
        RegisteredUserDto registeredUser = testSubject.register(userRegistrationDto);

        // Then
        verify(userDataService).register(eq(userRegistrationDto.getEmail()), eq(userRegistrationDto.getPassword()));
        assertThat(registeredUser.getId(), is( not( nullValue() )));
        assertThat(registeredUser.getAccountId(), is( not( nullValue())));
    }

    @Test(expected = EmailAlreadyRegisteredException.class)
    public void register_userDataServiceThrowsEmailAlreadyRegistered_exceptionIsPropagated() throws Exception {

        // Given

        UserRegistrationDto userDto = aUserRegistration();

        doThrow(new EmailAlreadyRegisteredException(USER_EMAIL))
                .when(userDataService).register( eq(userDto.getEmail()), eq(userDto.getPassword()) );

        // When
        testSubject.register(userDto);
    }

    @Test(expected = Exception.class)
    public void register_unexpectedException_exceptionIsPropagated() throws Exception {

        // Given

        UserRegistrationDto userDto = aUserRegistration();

        doThrow(new Exception())
                .when( userDataService).register( any(String.class), any(String.class));

        // When
        testSubject.register(userDto);
    }

    // ------------------------------------------------------------------------

    private UserRegistrationDto aUserRegistration() {
        return new UserRegistrationDto(USER_EMAIL, USER_PASSWORD);
    }

    private RegisteredUserDto aRegisteredUser() {
        return new RegisteredUserDto(10L, 20L);
    }
}
