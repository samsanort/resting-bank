package com.samsanort.restingbank.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsanort.restingbank.config.WebTestConfig;
import com.samsanort.restingbank.controller.impl.UserRESTController;
import com.samsanort.restingbank.dataservice.EmailAlreadyRegisteredException;
import com.samsanort.restingbank.dataservice.UserDataService;
import com.samsanort.restingbank.dataservice.UserNotFoundException;
import com.samsanort.restingbank.model.dto.BankAccountDto;
import com.samsanort.restingbank.security.WebSecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TODO Add description
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UserRESTController.class)
@Import({ WebTestConfig.class, WebSecurityConfig.class })
public class UserRESTControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserDataService userDataService;

    private static final Long USER_ID = 100L;

    private static final String URL_REGISTER = "/users";
    private static final String URL_USER_ACCOUNTS = URL_REGISTER + "/" + USER_ID.toString() + "/accounts";

    private static final String USER_EMAIL = "user@email.com";
    private static final String USER_PASSWORD = "secret_password";

    // --- register -----------------------------------------------------------

    @Test
    public void register_happyPath_responds201() throws Exception {

        // Given
        willDoNothing()
                .given(userDataService).register( eq(USER_EMAIL), eq(USER_PASSWORD));

        // When
        ResultActions result = mvc.perform(
                post(URL_REGISTER)
                        .content( registerUserRequestContent() )
                        .contentType(MediaType.APPLICATION_JSON) );

        // Then
        result.andExpect(status().isCreated());
    }

    @Test
    public void register_userDataServiceThrowsEmailAlreadyRegistered_responds400() throws Exception {

        // Given
        willThrow(EmailAlreadyRegisteredException.class)
                .given(userDataService).register( eq(USER_EMAIL), eq(USER_PASSWORD));

        // When
        ResultActions result = mvc.perform(
                post(URL_REGISTER)
                        .content( registerUserRequestContent() )
                        .contentType(MediaType.APPLICATION_JSON) );

        // Then
        result.andExpect(status().isBadRequest());
    }

    // --- getBankAccounts ----------------------------------------------------

    @Test
    @WithMockUser
    public void getBankAccounts_happyPath_returnsListOfBankAccountsAndResponds200() throws Exception {

        // Given
        List<BankAccountDto> expectedList = aListOfBankAccounts();
        given(userDataService.getBankAccounts(USER_ID)).willReturn(expectedList);

        // When
        ResultActions result = mvc.perform(get(URL_USER_ACCOUNTS));

        // Then
        result.andExpect(status().isOk());
        result.andExpect(content().string(new ObjectMapper().writeValueAsString(expectedList)));
    }

    @Test
    @WithMockUser
    public void getBankAccounts_userDataServiceThrowsUserNotFoundException_responds404() throws Exception {

        // Given
        willThrow(UserNotFoundException.class).given(userDataService).getBankAccounts(USER_ID);

        // When
        ResultActions result = mvc.perform(get(URL_USER_ACCOUNTS));

        // Then
        result.andExpect(status().isNotFound());
    }

    // ------------------------------------------------------------------------

    private String registerUserRequestContent() {

        return "{ \"email\":\""
                + USER_EMAIL
                + "\", \"password\":\""
                + USER_PASSWORD
                + "\" }";
    }

    private List<BankAccountDto> aListOfBankAccounts() {
        List<BankAccountDto> list = new ArrayList<>();
        list.add(new BankAccountDto(99L, new BigDecimal(500.75)));
        return list;
    }
}
