package com.samsanort.restingbank.model.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * TODO add description
 */
public class StatementDTO {

    private BigDecimal balance;
    private Date date;
    private List<TransactionDTO> transactions;
}
