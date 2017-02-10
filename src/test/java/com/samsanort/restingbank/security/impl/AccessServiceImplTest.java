package com.samsanort.restingbank.security.impl;

import com.samsanort.restingbank.dataservice.AccountDataService;
import com.samsanort.restingbank.security.AuthenticatedUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

/**
 * @see AccessServiceImpl
 */

@RunWith(MockitoJUnitRunner.class)
public class AccessServiceImplTest {

    @InjectMocks
    private AccessServiceImpl testSubject;

    @Mock
    private AccountDataService accountDataService;

    private static final Long AUTHENTICATED_USER_ID = 11L;
    private static final Long OTHER_USER_ID = 22L;
    private static final Long ACCOUNT_ID = 99L;

    // --- canManageUser ------------------------------------------------------

    @Test
    public void canManageUser_idMatchesAuthenticatedUser_returnsTrue() {

        // Given
        AuthenticatedUser user = anAuthenticatedUser(AUTHENTICATED_USER_ID);

        // When
        boolean canManage =
                testSubject.canManageUser(
                        anAuthenticationWithPrincipal( user ),
                        AUTHENTICATED_USER_ID);

        // Then
        assertThat( canManage, is( equalTo( true)));
    }

    @Test
    public void canManageUser_idDoesNotMatchAuthenticatedUser_returnsFalse() {

        // Given
        AuthenticatedUser user = anAuthenticatedUser(AUTHENTICATED_USER_ID);

        // When
        boolean canManage =
                testSubject.canManageUser(
                        anAuthenticationWithPrincipal( user ),
                        OTHER_USER_ID);

        // Then
        assertThat( canManage, is( equalTo( false)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void canManageUser_providedNullUserId_throwsIllegalArgumentException() {

        // When
        testSubject.canManageUser(
                anAuthenticationWithPrincipal( anAuthenticatedUser( AUTHENTICATED_USER_ID)),
                null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void canManageUser_providedNullAuthentication_throwsIllegalArgumentException() {

        // When
        testSubject.canManageUser(
                null,
                AUTHENTICATED_USER_ID);
    }

    @Test(expected = RuntimeException.class)
    public void canManageUser_authenticationHasNullPrincipal_throwsRuntimeException() {

        // When
        testSubject.canManageUser(
                anAuthenticationWithPrincipal( null ),
                AUTHENTICATED_USER_ID);
    }

    @Test(expected = RuntimeException.class)
    public void canManageUser_authenticatedPrincipalHasNullId_throwsRuntimeException() {

        // Given
        AuthenticatedUser userWithNullId = anAuthenticatedUser(null);

        // When
        testSubject.canManageUser(
                anAuthenticationWithPrincipal( userWithNullId ),
                AUTHENTICATED_USER_ID);
    }

    // --- canManageAccount ---------------------------------------------------

    @Test
    public void canManageAccount_authenticatedUserOwnsTheAccount_returnsTrue() {

        // Given

        AuthenticatedUser user = anAuthenticatedUser( AUTHENTICATED_USER_ID );
        Authentication authentication = anAuthenticationWithPrincipal( user );

        when(accountDataService.getOwnerId(ACCOUNT_ID)).thenReturn(AUTHENTICATED_USER_ID);

        // When
        boolean canManage = testSubject.canManageAccount(authentication, ACCOUNT_ID);

        // Then
        assertThat( canManage, is( equalTo( true)));
    }

    @Test
    public void canManageAccount_authenticatedUserDoesNotOwnTheAccount_returnsFalse() {

        // Given

        AuthenticatedUser user = anAuthenticatedUser( AUTHENTICATED_USER_ID );
        Authentication authentication = anAuthenticationWithPrincipal( user );

        when(accountDataService.getOwnerId(ACCOUNT_ID)).thenReturn(OTHER_USER_ID);

        // When
        boolean canManage = testSubject.canManageAccount(authentication, ACCOUNT_ID);

        // Then
        assertThat( canManage, is( equalTo( false)));
    }

    @Test(expected = RuntimeException.class)
    public void canManageAccount_accountDataServiceReturnsNullOwnerId_throwsRuntimeException() {

        // Given

        Authentication authentication = anAuthenticationWithPrincipal(anAuthenticatedUser(AUTHENTICATED_USER_ID));

        when(accountDataService.getOwnerId(ACCOUNT_ID)).thenReturn(null);

        // When
        testSubject.canManageAccount(
                authentication,
                ACCOUNT_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void canManageAccount_providedNullAccountId_throwsIllegalArgumentException() {

        // When
        testSubject.canManageAccount(
                anAuthenticationWithPrincipal( anAuthenticatedUser( AUTHENTICATED_USER_ID)),
                null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void canManageAccount_providedNullAuthentication_throwsIllegalArgumentException() {

        // When
        testSubject.canManageAccount(
                null,
                ACCOUNT_ID);
    }

    @Test(expected = RuntimeException.class)
    public void canManageAccount_authenticationHasNullPrincipal_throwsRuntimeException() {

        // When
        testSubject.canManageAccount(
                anAuthenticationWithPrincipal( null ),
                ACCOUNT_ID);
    }

    @Test(expected = RuntimeException.class)
    public void canManageAccount_authenticatedPrincipalHasNullId_throwsRuntimeException() {

        // Given
        AuthenticatedUser userWithNullId = anAuthenticatedUser(null);

        // When
        testSubject.canManageAccount(
                anAuthenticationWithPrincipal( userWithNullId ),
                ACCOUNT_ID);
    }

    // ------------------------------------------------------------------------

    private AuthenticatedUser anAuthenticatedUser(Long id) {
        return new AuthenticatedUser(id, "user@email.com", "secret_password");
    }

    private Authentication anAuthenticationWithPrincipal(AuthenticatedUser user) {
        return new StubbedAuthentication(user);
    }

    /**
     * TODO describe
     */
    private class StubbedAuthentication implements Authentication {

        private AuthenticatedUser user;

        StubbedAuthentication(AuthenticatedUser user) {
            this.user = user;
        }

        @Override
        public Object getPrincipal() {
            return this.user;
        }

        @Override public Collection<? extends GrantedAuthority> getAuthorities() {return null;}
        @Override public Object getCredentials() {return null;}
        @Override public Object getDetails() {return null;}
        @Override public boolean isAuthenticated() {return false;}
        @Override public void setAuthenticated(boolean b) throws IllegalArgumentException {}
        @Override public String getName() {return null;}
    }
}
