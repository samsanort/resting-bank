package com.samsanort.restingbank.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsanort.restingbank.dataservice.AccountDataService;
import com.samsanort.restingbank.dataservice.AccountNotFoundException;
import com.samsanort.restingbank.dataservice.InsufficientFundsException;
import com.samsanort.restingbank.model.dto.StatementDto;
import com.samsanort.restingbank.model.dto.TransactionDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Date;

import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TODO Add description
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AccountRESTController.class)
public class AccountRESTControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountDataService accountDataService;

    private final static Long ACCOUNT_ID = 1000L;
    private static final String URLPART_ACCOUNTS = "/accounts";
    private static final String URLPART_WITHDRAWALS = "/withdrawals";
    private static final String URLPART_DEPOSITS = "/deposits";


    private static final String URL_CONTROLLER = URLPART_ACCOUNTS;
    private static final String URL_ACCOUNT = URL_CONTROLLER + "/" + ACCOUNT_ID;
    private static final String URL_ACCOUNT_DEPOSITS = URL_ACCOUNT + URLPART_DEPOSITS;
    private static final String URL_ACCOUNT_WITHDRAWS = URL_ACCOUNT + URLPART_WITHDRAWALS;

    // --- deposit ------------------------------------------------------------

    @Test
    public void deposit_happyPath_responds201() throws Exception {

        // Given
        willDoNothing()
                .given(accountDataService).deposit( eq(ACCOUNT_ID), any(BigDecimal.class));

        // When
        ResultActions result = mvc.perform(
                post(URL_ACCOUNT_DEPOSITS)
                        .content("100")
                        .contentType(MediaType.APPLICATION_JSON) );

        // Then
        result.andExpect(status().isCreated());
    }

    @Test
    public void deposit_dataServiceThrowsAccountNotFoundException_responds404() throws Exception {

        // Given
        willThrow( AccountNotFoundException.class)
                .given(accountDataService).deposit( eq(ACCOUNT_ID), any(BigDecimal.class));

        // When
        ResultActions result = mvc.perform(
                post(URL_ACCOUNT_DEPOSITS)
                        .content("100")
                        .contentType(MediaType.APPLICATION_JSON) );

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void deposit_accountIdIsNotNumber_responds404() throws Exception {

        // When
        ResultActions result = mvc.perform(
                post(URL_CONTROLLER + "/not-a-number" + URLPART_DEPOSITS)
                        .content("100")
                        .contentType(MediaType.APPLICATION_JSON) );

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void deposit_dataServiceThrowsIllegalArgumentException_responds400() throws Exception {

        // Given
        willThrow( IllegalArgumentException.class)
                .given(accountDataService).deposit( eq(ACCOUNT_ID), any(BigDecimal.class));

        // When
        ResultActions result = mvc.perform(
                post(URL_ACCOUNT_DEPOSITS)
                        .content("-100")
                        .contentType(MediaType.APPLICATION_JSON) );

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void deposit_requestContentIsInvalid_responds400() throws Exception {

        // When
        ResultActions result = mvc.perform(
                post(URL_ACCOUNT_DEPOSITS)
                        .content("invalid content")
                        .contentType(MediaType.APPLICATION_JSON) );

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void deposit_unexpectedException_responds500() throws Exception {

        // Given
        willThrow( RuntimeException.class)
                .given(accountDataService).deposit( eq(ACCOUNT_ID), any(BigDecimal.class));

        // When
        ResultActions result = mvc.perform(
                post(URL_ACCOUNT_DEPOSITS)
                        .content("100")
                        .contentType(MediaType.APPLICATION_JSON) );

        // Then
        result.andExpect(status().isInternalServerError());
    }

    // --- withdraw -----------------------------------------------------------

    @Test
    public void withdraw_happyPath_responds201() throws Exception {

        // Given
        willDoNothing()
                .given(accountDataService).withdraw( eq(ACCOUNT_ID), any(BigDecimal.class));

        // When
        ResultActions result = mvc.perform(
                post(URL_ACCOUNT_WITHDRAWS)
                        .content("100")
                        .contentType(MediaType.APPLICATION_JSON) );

        // Then
        result.andExpect(status().isCreated());
    }

    @Test
    public void withdraw_dataServiceThrowsInsufficientFundsException_responds409() throws Exception {

        // Given
        willThrow( InsufficientFundsException.class)
                .given(accountDataService).withdraw( eq(ACCOUNT_ID), any(BigDecimal.class));

        // When
        ResultActions result = mvc.perform(
                post(URL_ACCOUNT_WITHDRAWS)
                        .content("100")
                        .contentType(MediaType.APPLICATION_JSON) );

        // Then
        result.andExpect(status().isConflict());
    }

    @Test
    public void withdraw_dataServiceThrowsAccountNotFoundException_responds404() throws Exception {

        // Given
        willThrow( AccountNotFoundException.class)
                .given(accountDataService).withdraw( eq(ACCOUNT_ID), any(BigDecimal.class));

        // When
        ResultActions result = mvc.perform(
                post(URL_ACCOUNT_WITHDRAWS)
                        .content("100")
                        .contentType(MediaType.APPLICATION_JSON) );

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void withdraw_accountIdIsNotNumber_responds404() throws Exception {

        // When
        ResultActions result = mvc.perform(
                post(URL_CONTROLLER + "/not-a-number" + URLPART_WITHDRAWALS)
                        .content("100")
                        .contentType(MediaType.APPLICATION_JSON) );

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void withdraw_dataServiceThrowsIllegalArgumentException_responds400() throws Exception {

        // Given
        willThrow( IllegalArgumentException.class)
                .given(accountDataService).withdraw( eq(ACCOUNT_ID), any(BigDecimal.class));

        // When
        ResultActions result = mvc.perform(
                post(URL_ACCOUNT_WITHDRAWS)
                        .content("-100")
                        .contentType(MediaType.APPLICATION_JSON) );

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void withdraw_requestContentIsInvalid_responds400() throws Exception {

        // When
        ResultActions result = mvc.perform(
                post(URL_ACCOUNT_WITHDRAWS)
                        .content("invalid content")
                        .contentType(MediaType.APPLICATION_JSON) );

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void withdraw_unexpectedException_responds500() throws Exception {

        // Given
        willThrow( RuntimeException.class)
                .given(accountDataService).withdraw( eq(ACCOUNT_ID), any(BigDecimal.class));

        // When
        ResultActions result = mvc.perform(
                post(URL_ACCOUNT_WITHDRAWS)
                        .content("100")
                        .contentType(MediaType.APPLICATION_JSON) );

        // Then
        result.andExpect(status().isInternalServerError());
    }

    // --- statement ----------------------------------------------------------

    @Test
    public void statement_happyPath_returnsStatementAndResponds200() throws Exception {

        // Given
        StatementDto expectedStatement = anStatementDTO();
        given(accountDataService.statement(eq(ACCOUNT_ID))).willReturn(expectedStatement);

        // When
        ResultActions result = mvc.perform(get(URL_ACCOUNT));

        // Then
        result
                .andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(expectedStatement) ));
    }

    @Test
    public void statement_dataServiceThrowsAccountNotFoundException_responds404() throws Exception {

        // Given
        willThrow(AccountNotFoundException.class).given(accountDataService).statement(ACCOUNT_ID);

        // When
        ResultActions result = mvc.perform(get(URL_ACCOUNT));

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void statement_accountIdIsNotNumber_responds404() throws Exception {

        // When
        ResultActions result = mvc.perform(get(URL_CONTROLLER + "/not-a-number"));

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void statement_unexpectedException_responds500() throws Exception {

        // Given
        willThrow(RuntimeException.class).given(accountDataService).statement(ACCOUNT_ID);

        // When
        ResultActions result = mvc.perform(get(URL_ACCOUNT));

        // Then
        result.andExpect(status().isInternalServerError());
    }

    // ------------------------------------------------------------------------

    private StatementDto anStatementDTO() {
        StatementDto statementDto = new StatementDto(new Date(), new BigDecimal(1000.5));
        statementDto.getTransactions().add(new TransactionDto(new Date(), new BigDecimal(50.75), "Deposit"));
        return statementDto;
    }
}
