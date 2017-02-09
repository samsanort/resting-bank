package com.samsanort.restingbank.controller.impl;

import com.samsanort.restingbank.dataservice.EmailAlreadyRegisteredException;
import com.samsanort.restingbank.dataservice.UserDataService;
import com.samsanort.restingbank.dataservice.UserNotFoundException;
import com.samsanort.restingbank.model.dto.BankAccountDto;
import com.samsanort.restingbank.model.dto.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * TODO Add description
 */
@RunWith(MockitoJUnitRunner.class)
public class UserRESTControllerTest {

    @InjectMocks
    private UserRESTController testSubject;

    @Mock
    private UserDataService userDataService;

    private static final Long USER_ID = 100L;
    private static final String USER_EMAIL = "user@email.com";
    private static final String USER_PASSWORD = "secret_password";

    // --- register -----------------------------------------------------------

    @Test
    public void register_happyPath_createsUserWithAccount() throws Exception {

        // Given
        UserDto userDto = aUser();

        // When
        testSubject.register(userDto);

        // Then
        verify(userDataService).register(eq(userDto.getEmail()), eq(userDto.getPassword()));
    }

    @Test(expected = EmailAlreadyRegisteredException.class)
    public void register_userDataServiceThrowsEmailAlreadyRegistered_exceptionIsPropagated() throws Exception {

        // Given

        UserDto userDto = aUser();

        doThrow(new EmailAlreadyRegisteredException(USER_EMAIL))
                .when(userDataService).register( eq(userDto.getEmail()), eq(userDto.getPassword()) );

        // When
        testSubject.register(userDto);
    }

    @Test(expected = Exception.class)
    public void register_unexpectedException_exceptionIsPropagated() throws Exception {

        // Given

        UserDto userDto = aUser();

        doThrow(new Exception())
                .when( userDataService).register( any(String.class), any(String.class));

        // When
        testSubject.register(userDto);
    }

    // --- getBankAccounts ----------------------------------------------------

    @Test
    public void getBankAccounts_happyPath_returnsListOfBankAccounts() throws Exception {

        // Given

        List<BankAccountDto> expectedAccounts = aListOfBankAccounts();

        when(userDataService.getBankAccounts(eq(USER_ID))).thenReturn(expectedAccounts);

        // When
        List<BankAccountDto> accounts = testSubject.getBankAccounts(USER_ID);

        // Then

        verify(userDataService.getBankAccounts(eq(USER_ID)));

        assertThat( accounts.size(), is( equalTo( expectedAccounts.size())));
    }

    @Test(expected = UserNotFoundException.class)
    public void getBankAccounts_userDataServiceThrowsUserNotFoundException_exceptionIsPropagated() throws Exception {

        // Given
        doThrow(new UserNotFoundException(USER_ID))
                .when(userDataService).getBankAccounts( eq(USER_ID));

        // When
        testSubject.getBankAccounts(USER_ID);
    }

    @Test(expected = Exception.class)
    public void getBankAccounts_unexpectedException_exceptionIsPropagated() throws Exception {

        // Given
        doThrow(new Exception())
                .when(userDataService).getBankAccounts( eq(USER_ID));

        // When
        testSubject.getBankAccounts(USER_ID);
    }

    // ------------------------------------------------------------------------

    private UserDto aUser() {
        return new UserDto(USER_ID, USER_EMAIL, USER_PASSWORD);
    }

    private List<BankAccountDto> aListOfBankAccounts() {
        List<BankAccountDto> list = new ArrayList<>();
        list.add(new BankAccountDto(99L, new BigDecimal(500.75)));
        return list;
    }
}
