package com.samsanort.restingbank.dataservice.impl;

import com.samsanort.restingbank.dataservice.EmailAlreadyRegisteredException;
import com.samsanort.restingbank.dataservice.UserNotFoundException;
import com.samsanort.restingbank.model.dto.BankAccountDto;
import com.samsanort.restingbank.model.entity.BankAccount;
import com.samsanort.restingbank.model.entity.User;
import com.samsanort.restingbank.repository.BankAccountRepository;
import com.samsanort.restingbank.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by samu on 2/6/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class UserDataServiceImplTest {

    @InjectMocks
    private UserDataServiceImpl testSubject;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BankAccountRepository accountRepository;

    private final static Long USER_ID = 10L;
    private final static String USER_EMAIL = "user@email.com";
    private final static String USER_PASSWORD = "a_secret_password";

    private final static Long ACCOUNT_ID = 20L;


    // --- register -----------------------------------------------------------

    @Test
    public void register_happyPath_persistsUserWithNewBankAccount() {

        // Given
        when( userRepository.findByEmail(USER_EMAIL)).thenReturn( null);
        when( userRepository.save(any(User.class))).thenAnswer( mockedSaveUserAnswer());
        when( accountRepository.save(any(BankAccount.class))).thenAnswer( mockedSaveAccountAnswer());

        // When
        testSubject.register(USER_EMAIL, USER_PASSWORD );

        // Then

        ArgumentCaptor<User> userArg = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<BankAccount> accountArg = ArgumentCaptor.forClass(BankAccount.class);

        verify(userRepository, times(2)).save(userArg.capture());
        verify(accountRepository, times(2)).save(accountArg.capture());

        assertUser( userArg.getValue(), accountArg.getValue());
        assertBankAccount( accountArg.getValue(), userArg.getValue());
    }

    @Test(expected = EmailAlreadyRegisteredException.class)
    public void register_existsUserWithSameEmail_throwsEmailAlredyRegisteredException() {

        // Given
        when( userRepository.findByEmail(USER_EMAIL)).thenReturn( aUserWithBankAccount(null));

        // When
        testSubject.register(USER_EMAIL, USER_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void register_nullEmail_throwsIllegalArgumentException() {

        // When
        testSubject.register(null, USER_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void register_invalidEmailFormat_throwsIllegalArgumentException() {

        // When
        testSubject.register("not_an_email", USER_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void register_nullPassword_throwsIllegalArgumentException() {

        // When
        testSubject.register(USER_EMAIL, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void register_emptyPassword_throwsIllegalArgumentException() {

        // When
        testSubject.register(USER_EMAIL, "");
    }

    // --- getBankAccounts ----------------------------------------------------

    @Test
    public void getBankAccounts_userHasAccounts_returnsListWithAccounts() {

        // Given
        User userWithAccount = aUserWithBankAccount( aBankAccount() );
        when( userRepository.findOne( userWithAccount.getId())).thenReturn( userWithAccount);

        // When
        List<BankAccountDto> accounts = testSubject.getBankAccounts(userWithAccount.getId());

        // Then
        assertThat( accounts, notNullValue());
        assertThat( accounts.size(), is( equalTo( 1 ) ));
        assertThat( accounts.get(0).getId(), is( equalTo( userWithAccount.getAccount().getId() )) );
        assertThat( accounts.get(0).getBalance(), is( equalTo( userWithAccount.getAccount().getBalance() )) );
    }

    @Test
    public void getBankAccounts_userDoesNotHaveAccount_returnsEmptyList() {

        // Given
        User userWithAccount = aUserWithBankAccount( null );
        when( userRepository.findOne( userWithAccount.getId())).thenReturn( userWithAccount);

        // When
        List<BankAccountDto> accounts = testSubject.getBankAccounts(userWithAccount.getId());

        // Then
        assertThat( accounts, notNullValue());
        assertThat( accounts.size(), is( equalTo( 0 ) ));
    }

    @Test(expected = UserNotFoundException.class)
    public void getBankAccounts_userDoesNotExist_throwsUserNotFoundException() {

        // Given
        Long idOfNonExistingUser = 99L;
        when( userRepository.findOne( idOfNonExistingUser)).thenReturn( null);

        // When
        testSubject.getBankAccounts( idOfNonExistingUser );
    }

    // ------------------------------------------------------------------------

    private User aUserWithBankAccount(BankAccount bankAccount) {
        User user = new User(USER_ID, USER_EMAIL, USER_PASSWORD);
        user.setAccount(bankAccount);
        return user;
    }

    private BankAccount aBankAccount() {

        return new BankAccount(ACCOUNT_ID, new BigDecimal(1000.5));
    }

    private void assertUser(User user, BankAccount account) {

        assertThat( user.getId(), is( equalTo(USER_ID)));
        assertThat( user.getEmail(), is( equalTo(USER_EMAIL)));
        assertThat( user.getPassword(), is( equalTo( USER_PASSWORD)));
        assertThat( user.getAccount().getId(), is( equalTo( account.getId())));
    }

    private void assertBankAccount(BankAccount account, User user) {
        assertThat( account.getId(), is( equalTo( ACCOUNT_ID)));
        assertThat( account.getBalance(), is( equalTo( new BigDecimal(0))));
        assertThat( account.getTransactions().size(), is( equalTo( 0)));
        assertThat( account.getOwner().getId(), is( equalTo( user.getId())));
    }

    private Answer<User> mockedSaveUserAnswer() {
        return new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocation) throws Throwable {
                User user = (User) invocation.getArguments()[0];
                user.setId(USER_ID);
                return user;
            }
        };
    }

    private Answer<BankAccount> mockedSaveAccountAnswer() {
        return new Answer<BankAccount>() {
            @Override
            public BankAccount answer(InvocationOnMock invocation) throws Throwable {
                BankAccount account = (BankAccount) invocation.getArguments()[0];
                account.setId(ACCOUNT_ID);
                return account;
            }
        };
    }
}
