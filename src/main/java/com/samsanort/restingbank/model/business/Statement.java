package com.samsanort.restingbank.model.business;

import com.samsanort.restingbank.model.dto.StatementDto;
import com.samsanort.restingbank.model.dto.TransactionDto;
import com.samsanort.restingbank.model.entity.AccountTransaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Representation of an account statement.
 */
public class Statement {

    private Date date = new Date();
    private BigDecimal accountBalance;
    private List<AccountTransaction> transactions;

    /**
     * C'tor.
     * @param accountBalance Account balance at the time of the statement.
     * @param transactions The transactions performed onto the account up to the statement date.
     */
    public Statement(BigDecimal accountBalance, List<AccountTransaction> transactions) {
        this.accountBalance = accountBalance;
        this.transactions = transactions;
    }

    /**
     * Obtains a StatementDto representation of this statement.
     * @return The StatementDTO representation.
     */
    public StatementDto toDto() {

        StatementDto dto = new StatementDto(this.date, this.accountBalance);

        for(AccountTransaction transaction : this.transactions) {
            dto.getTransactions().add(
                    new TransactionDto(
                            transaction.getDate(),
                            transaction.getAmount(),
                            transaction.getDescription()));
        }

        return dto;
    }
}
