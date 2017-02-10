package com.samsanort.restingbank.controller.impl;

import com.samsanort.restingbank.dataservice.AccountDataService;
import com.samsanort.restingbank.dataservice.AccountNotFoundException;
import com.samsanort.restingbank.dataservice.InsufficientFundsException;
import com.samsanort.restingbank.model.dto.StatementDto;
import com.samsanort.restingbank.model.dto.TransactionDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

/**
 * @see AccountRESTController
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountRESTControllerTest {

    @InjectMocks
    private AccountRESTController testSubject;

    @Mock
    private AccountDataService accountDataService;

    private static final Long ACCOUNT_ID = 99L;
    private static final BigDecimal HUNDRED_EUROS = new BigDecimal(100);

    // --- deposit ------------------------------------------------------------

    @Test
    public void deposit_happyPath_depositIsDone() throws Exception {

        // When
        testSubject.deposit(ACCOUNT_ID, HUNDRED_EUROS);

        // Then
        verify(accountDataService).deposit(eq(ACCOUNT_ID), eq(HUNDRED_EUROS));
    }

    @Test(expected = AccountNotFoundException.class)
    public void deposit_dataServiceThrowsAccountNotFoundException_exceptionIsPropagated() throws Exception {

        // Given
        doThrow(new AccountNotFoundException(ACCOUNT_ID)).when(accountDataService).deposit(eq(ACCOUNT_ID), any(BigDecimal.class));

        // When
        testSubject.deposit(ACCOUNT_ID, HUNDRED_EUROS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deposit_dataServiceThrowsIllegalArgumentException_exceptionIsPropagated() throws Exception {

        // Given
        doThrow(new IllegalArgumentException()).when(accountDataService).deposit(eq(ACCOUNT_ID), any(BigDecimal.class));

        // When
        testSubject.deposit(ACCOUNT_ID, HUNDRED_EUROS);
    }

    @Test(expected = Exception.class)
    public void deposit_unexpectedException_exceptionIsPropagated() throws Exception {

        // Given
        doThrow(new Exception()).when(accountDataService).deposit(eq(ACCOUNT_ID), any(BigDecimal.class));

        // When
        testSubject.deposit(ACCOUNT_ID, HUNDRED_EUROS);
    }

    // --- withdraw -----------------------------------------------------------

    @Test
    public void withdraw_happyPath_withdrawIsDone() throws Exception {

        // When
        testSubject.withdraw(ACCOUNT_ID, HUNDRED_EUROS);

        // Then
        verify(accountDataService).withdraw(eq(ACCOUNT_ID), eq(HUNDRED_EUROS));
    }

    @Test(expected = InsufficientFundsException.class)
    public void withdraw_dataServiceThrowsInsufficientFundsException_exceptionIsPropagated() throws Exception {

        // Given
        doThrow(new InsufficientFundsException(new BigDecimal(0), HUNDRED_EUROS)).when(accountDataService).withdraw(eq(ACCOUNT_ID), any(BigDecimal.class));

        // When
        testSubject.withdraw(ACCOUNT_ID, HUNDRED_EUROS);
    }

    @Test(expected = AccountNotFoundException.class)
    public void withdraw_dataServiceThrowsAccountNotFoundException_exceptionIsPropagated() throws Exception {

        // Given
        doThrow(new AccountNotFoundException(ACCOUNT_ID)).when(accountDataService).withdraw(eq(ACCOUNT_ID), any(BigDecimal.class));

        // When
        testSubject.withdraw(ACCOUNT_ID, HUNDRED_EUROS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void withdraw_dataServiceThrowsIllegalArgumentException_exceptionIsPropagated() throws Exception {

        // Given
        doThrow(new IllegalArgumentException()).when(accountDataService).withdraw(eq(ACCOUNT_ID), any(BigDecimal.class));

        // When
        testSubject.withdraw(ACCOUNT_ID, HUNDRED_EUROS);
    }

    @Test(expected = Exception.class)
    public void withdraw_unexpectedException_exceptionIsPropagated() throws Exception {

        // Given
        doThrow(new Exception()).when(accountDataService).withdraw(eq(ACCOUNT_ID), any(BigDecimal.class));

        // When
        testSubject.withdraw(ACCOUNT_ID, HUNDRED_EUROS);
    }

    // --- statement ----------------------------------------------------------

    @Test
    public void statement_happyPath_returnsStatement() throws Exception {

        // Given

        StatementDto expectedStatement = anStatementDTO();

        when(accountDataService.statement(ACCOUNT_ID)).thenReturn(expectedStatement);

        // When
        StatementDto returnedStatement = testSubject.statement(ACCOUNT_ID);

        // Then

        verify(accountDataService).statement(eq(ACCOUNT_ID));

        assertThat( returnedStatement, is( equalTo( expectedStatement)));
    }

    @Test(expected = AccountNotFoundException.class)
    public void statement_dataServiceThrowsAccountNotFoundException_exceptionIsPropagated() throws Exception {

        // Given
        doThrow(new AccountNotFoundException(ACCOUNT_ID)).when(accountDataService).statement(eq(ACCOUNT_ID));

        // When
        testSubject.statement(ACCOUNT_ID);
    }

    @Test(expected = Exception.class)
    public void statement_unexpectedException_exceptionIsPropagated() throws Exception {

        // Given
        doThrow(new Exception()).when(accountDataService).statement(eq(ACCOUNT_ID));

        // When
        testSubject.statement(ACCOUNT_ID);
    }

    // ------------------------------------------------------------------------

    private StatementDto anStatementDTO() {
        StatementDto statementDto = new StatementDto(new Date(), new BigDecimal(1000.5));
        statementDto.getTransactions().add(new TransactionDto(new Date(), new BigDecimal(50.75), "Deposit"));
        return statementDto;
    }
}
