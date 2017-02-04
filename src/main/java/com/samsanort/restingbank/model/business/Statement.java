package com.samsanort.restingbank.model.business;

import com.samsanort.restingbank.model.dto.StatementDto;
import com.samsanort.restingbank.model.dto.TransactionDto;
import com.samsanort.restingbank.model.entity.AccountTransaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * TODO describe
 */
public class Statement {

    private Date date = new Date();
    private BigDecimal accountBalance;
    private List<AccountTransaction> transactions;

    /**
     * TODO describe
     * @param accountBalance
     * @param transactions
     */
    public Statement(BigDecimal accountBalance, List<AccountTransaction> transactions) {
        this.accountBalance = accountBalance;
        this.transactions = transactions;
    }

    /**
     * TODO describe
     * @return
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
