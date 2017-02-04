package com.samsanort.restingbank.dataservice.impl;

import com.samsanort.restingbank.dataservice.AccountDataService;
import com.samsanort.restingbank.dataservice.AccountNotFoundException;
import com.samsanort.restingbank.dataservice.InsufficientFundsException;
import com.samsanort.restingbank.model.business.Statement;
import com.samsanort.restingbank.model.dto.StatementDto;
import com.samsanort.restingbank.model.entity.AccountTransaction;
import com.samsanort.restingbank.model.entity.BankAccount;
import com.samsanort.restingbank.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

/**
 * TODO add description
 */
public class AccountDataServiceImpl implements AccountDataService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Override
    public void withdraw(Long accountId, BigDecimal amount) {

        this.processTransaction(accountId, amount, TransactionType.Withdrawal);
    }

    @Override
    public void deposit(Long accountId, BigDecimal amount) {

        this.processTransaction(accountId, amount, TransactionType.Deposit);
    }

    @Override
    public StatementDto statement(Long accountId) {

        BankAccount account = retrieveBankAccountOrFail( accountId );

        return new Statement(account.getBalance(), account.getTransactions()).toDto();
    }

    private void processTransaction(Long accountId, BigDecimal amount, TransactionType transactionType) {

        if( amount == null || amount.compareTo(new BigDecimal(0)) < 1 ) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        BankAccount bankAccount = retrieveBankAccountOrFail(accountId);

        bankAccount.getTransactions().add(
                new AccountTransaction( new Date(), amount, transactionType.name()));

        BigDecimal currentBalance = bankAccount.getBalance();
        switch(transactionType) {

            case Deposit:
                bankAccount.setBalance( currentBalance.add( amount));
                break;

            case Withdrawal:
                if( amount.compareTo(currentBalance) > 0 ) {
                    throw new InsufficientFundsException(currentBalance, amount);
                }
                bankAccount.setBalance( currentBalance.subtract( amount));
                break;
        }

        this.bankAccountRepository.save(bankAccount);
    }

    private BankAccount retrieveBankAccountOrFail(Long id) {

        BankAccount bankAccount = this.bankAccountRepository.findOne(id);

        if(bankAccount == null){
            throw new AccountNotFoundException(id);
        }

        return bankAccount;
    }

    /**
     * Type of transaction
     */
    private enum TransactionType { Deposit, Withdrawal }
}
