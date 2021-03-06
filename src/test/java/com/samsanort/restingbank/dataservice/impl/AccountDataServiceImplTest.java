package com.samsanort.restingbank.dataservice.impl;

import com.samsanort.restingbank.dataservice.AccountNotFoundException;
import com.samsanort.restingbank.dataservice.InsufficientFundsException;
import com.samsanort.restingbank.dataservice.NegativeOrZeroAmountException;
import com.samsanort.restingbank.model.dto.StatementDto;
import com.samsanort.restingbank.model.dto.TransactionDto;
import com.samsanort.restingbank.model.entity.AccountTransaction;
import com.samsanort.restingbank.model.entity.BankAccount;
import com.samsanort.restingbank.model.entity.User;
import com.samsanort.restingbank.repository.AccountTransactionRepository;
import com.samsanort.restingbank.repository.BankAccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * @see AccountDataServiceImpl
 */

@RunWith(MockitoJUnitRunner.class)
public class AccountDataServiceImplTest {

    @InjectMocks
    private AccountDataServiceImpl testSubject;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private AccountTransactionRepository accountTransactionRepository;

    private final static Long ACCOUNT_ID = 9999L;

    // --- deposit ------------------------------------------------------------

    @Test
    public void deposit_accountExists_addsTransactionAndIncreasesBalance() {

        // Given

        BigDecimal initialBalance = new BigDecimal(500);
        BigDecimal depositAmount = new BigDecimal(100);
        BankAccount account = new BankAccount(ACCOUNT_ID, initialBalance);

        when(bankAccountRepository.findOne(ACCOUNT_ID)).thenReturn(account);

        // When

        testSubject.deposit(ACCOUNT_ID, depositAmount);

        // Then

        ArgumentCaptor<AccountTransaction> transactionArgCaptor = ArgumentCaptor.forClass(AccountTransaction.class);
        verify(accountTransactionRepository).save(transactionArgCaptor.capture());
        AccountTransaction capturedTransaction = transactionArgCaptor.getValue();

        assertThat(capturedTransaction.getAmount(), is( equalTo( depositAmount )));

        ArgumentCaptor<BankAccount> accountArgCaptor = ArgumentCaptor.forClass(BankAccount.class);
        verify(bankAccountRepository).save(accountArgCaptor.capture());
        BankAccount capturedAccount = accountArgCaptor.getValue();

        assertThat(capturedAccount.getBalance(), is( equalTo( initialBalance.add(depositAmount) )));
        assertThat(capturedAccount.getTransactions().size(), is( equalTo(1)));
        assertThat(capturedAccount.getTransactions().get(0).getAmount(), is( equalTo( depositAmount)));
    }

    @Test(expected = AccountNotFoundException.class)
    public void deposit_accountDoesNotExist_throwsAccountNotFoundException() {

        // Given
        when(bankAccountRepository.findOne(eq(ACCOUNT_ID))).thenReturn(null);

        // When
        testSubject.deposit(ACCOUNT_ID, new BigDecimal(10));
    }

    @Test(expected = NegativeOrZeroAmountException.class)
    public void deposit_amountNotGreaterThanZero_throwsNegativeAmountException() {

        // When
        testSubject.deposit(ACCOUNT_ID, new BigDecimal(-1));
    }

    // --- withdraw -----------------------------------------------------------

    @Test
    public void withdraw_accountWithFunds_addsTransactionAndDecreasesBalance() {

        // Given

        BigDecimal initialBalance = new BigDecimal(500);
        BigDecimal withdrawalAmount = new BigDecimal(100);
        BankAccount account = new BankAccount(ACCOUNT_ID, initialBalance);

        when(bankAccountRepository.findOne(ACCOUNT_ID)).thenReturn(account);

        // When

        testSubject.withdraw(ACCOUNT_ID, withdrawalAmount);

        // Then

        ArgumentCaptor<AccountTransaction> transactionArgCaptor = ArgumentCaptor.forClass(AccountTransaction.class);
        verify(accountTransactionRepository).save(transactionArgCaptor.capture());
        AccountTransaction capturedTransaction = transactionArgCaptor.getValue();

        assertThat(capturedTransaction.getAmount(), is( equalTo( withdrawalAmount )));

        ArgumentCaptor<BankAccount> accountArgCaptor = ArgumentCaptor.forClass(BankAccount.class);
        verify(bankAccountRepository).save(accountArgCaptor.capture());
        BankAccount capturedAccount = accountArgCaptor.getValue();

        assertThat(capturedAccount.getBalance(), is( equalTo( initialBalance.subtract(withdrawalAmount))));
        assertThat(capturedAccount.getTransactions().size(), is( equalTo(1)));
        assertThat(capturedAccount.getTransactions().get(0).getAmount(), is( equalTo( withdrawalAmount)));
    }

    @Test(expected = AccountNotFoundException.class)
    public void withdraw_accountDoesNotExist_throwsAccountNotFoundException() {

        // Given
        when(bankAccountRepository.findOne(eq(ACCOUNT_ID))).thenReturn(null);

        // When
        testSubject.withdraw(ACCOUNT_ID, new BigDecimal(10));
    }

    @Test(expected = NegativeOrZeroAmountException.class)
    public void withdraw_amountNotGreaterThanZero_throwsNegativeAmountException() {

        // When
        testSubject.withdraw(ACCOUNT_ID, new BigDecimal(-1));
    }

    @Test(expected = InsufficientFundsException.class)
    public void withdraw_accountWithoutEnoughFunds_throwsInsufficientFundsException() {

        // Given

        BigDecimal initialBalance = new BigDecimal(100);
        BigDecimal withdrawalAmount = new BigDecimal(200);
        BankAccount account = new BankAccount(ACCOUNT_ID, initialBalance);

        when(bankAccountRepository.findOne(ACCOUNT_ID)).thenReturn(account);

        // When

        testSubject.withdraw(ACCOUNT_ID, withdrawalAmount);
    }

    // --- statement ----------------------------------------------------------

    @Test
    public void statement_accountWithTransactions_returnsStatementWithTransactions() {

        // Given

        BankAccount account = anAccountWithTransactions(ACCOUNT_ID);

        when(bankAccountRepository.findOne(eq(ACCOUNT_ID))).thenReturn(account);

        // When

        StatementDto statementDto = testSubject.statement(ACCOUNT_ID);

        // Then

        assertThat( statementDto.getBalance(), is( equalTo( account.getBalance())));
        assertThat( statementDto.getTransactions().size(), is( equalTo( account.getTransactions().size())));

        for( int i = 0; i < account.getTransactions().size(); i++) {
            assertTransaction(
                    account.getTransactions().get(i),
                    statementDto.getTransactions().get(i));
        }
    }

    @Test(expected = AccountNotFoundException.class)
    public void statement_accountDoesNotExist_throwsAccountNotFoundException() {

        // Given
        when(bankAccountRepository.findOne(eq(ACCOUNT_ID))).thenReturn(null);

        // When
        testSubject.statement(ACCOUNT_ID);
    }

    // --- getOwnerId ---------------------------------------------------------

    @Test
    public void getOwnerId_happyPath_returnsOwnerId() {

        // Given

        BankAccount account = anAccountWithTransactions(ACCOUNT_ID);

        when(bankAccountRepository.findOne(ACCOUNT_ID)).thenReturn(account);

        // When
        Long ownerId = testSubject.getOwnerId(ACCOUNT_ID);

        // Then
        assertThat( ownerId, is( equalTo( account.getOwner().getId())));
    }

    @Test(expected = AccountNotFoundException.class)
    public void getOwnerId_accountDoesNotExist_throwsAccountNotFoundException() {

        // Given
        when(bankAccountRepository.findOne(eq(ACCOUNT_ID))).thenReturn(null);

        // When
        testSubject.statement(ACCOUNT_ID);
    }

    // ------------------------------------------------------------------------

    private BankAccount anAccountWithTransactions(Long accountId) {
        BankAccount account = new BankAccount(accountId, new BigDecimal(500));
        account.setOwner( new User(25L, "user@email.com", "secret_password"));
        LocalDate baseDate = LocalDate.now(ZoneId.systemDefault());
        account.getTransactions().add(new AccountTransaction(new Date( baseDate.minusDays(1).toEpochDay() ), new BigDecimal(100), "Deposit"));
        account.getTransactions().add(new AccountTransaction(new Date( baseDate.minusDays(2).toEpochDay() ), new BigDecimal(50), "Withdrawal"));
        return account;
    }

    private void assertTransaction(AccountTransaction transactionEntity, TransactionDto transactionDto) {
        assertThat( transactionEntity.getAmount(), is( equalTo( transactionDto.getAmount())));
        assertThat( transactionEntity.getDate(), is( equalTo( transactionDto.getDate())));
        assertThat( transactionEntity.getDescription(), is( equalTo( transactionDto.getDescription())));
    }
}
