package com.samsanort.restingbank.controller.impl;

import com.samsanort.restingbank.controller.AccountController;
import com.samsanort.restingbank.dataservice.AccountDataService;
import com.samsanort.restingbank.dataservice.AccountNotFoundException;
import com.samsanort.restingbank.dataservice.InsufficientFundsException;
import com.samsanort.restingbank.dataservice.NegativeOrZeroAmountException;
import com.samsanort.restingbank.model.dto.StatementDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * REST implementation of the controller.
 * @see AccountController
 */

@RestController
@RequestMapping("/accounts")
public class AccountRESTController implements AccountController {

    private final Logger logger = LoggerFactory.getLogger(AccountRESTController.class);

    @Autowired
    AccountDataService accountDataService;

    @PreAuthorize("@accessService.canManageAccount(authentication,#accountId)")
    @RequestMapping(value = "/{accountId}/withdrawals", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void withdraw(
            @PathVariable Long accountId,
            @RequestBody BigDecimal amount) {

        accountDataService.withdraw(accountId, amount);
    }

    @PreAuthorize("@accessService.canManageAccount(authentication,#accountId)")
    @RequestMapping(value = "/{accountId}/deposits", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void deposit(
            @PathVariable Long accountId,
            @RequestBody BigDecimal amount) {

        accountDataService.deposit(accountId, amount);
    }

    @PreAuthorize("@accessService.canManageAccount(authentication,#accountId)")
    @RequestMapping(value = "/{accountId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public StatementDto statement(
            @PathVariable Long accountId) {

        return accountDataService.statement(accountId);
    }

    @ExceptionHandler({AccountNotFoundException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Account not found")
    public void accountNotFound() {}

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Not enough funds in the account")
    public void insufficientFunds() {}

    @ExceptionHandler(NegativeOrZeroAmountException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Positive amount expected")
    public void negativeAmount() {}

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Unexpected illegal argument")
    public void illegalArgument(IllegalArgumentException iae) {
        logger.debug(iae.getMessage(), iae);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid request body")
    public void invalidParameter() {}

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Access denied to requested resource")
    public void accessDenied() {}

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unexpected internal error")
    public void internalError(HttpServletRequest req, Exception ex) {
        logger.error(req.getRequestURL().toString(), ex);
    }
}
