package com.samsanort.restingbank.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsanort.restingbank.dataservice.EmailAlreadyRegisteredException;
import com.samsanort.restingbank.dataservice.UserDataService;
import com.samsanort.restingbank.model.dto.RegisteredUserDto;
import com.samsanort.restingbank.model.dto.UserRegistrationDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the user REST controller interactions.
 * @see com.samsanort.restingbank.controller.impl.UserRESTController
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserRESTControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserDataService userDataService;

    private static final Long USER_ID = 100L;

    private static final String URL_REGISTER = "/users";

    private static final String USER_EMAIL = "user@email.com";
    private static final String USER_PASSWORD = "secret_password";

    // --- register -----------------------------------------------------------

    @Test
    public void register_happyPath_responds201() throws Exception {

        // Given

        RegisteredUserDto registeredUser = aRegisteredUser();

        willReturn( registeredUser)
                .given(userDataService).register( eq(USER_EMAIL), eq(USER_PASSWORD));

        // When
        ResultActions result = mvc.perform(
                post(URL_REGISTER)
                        .content( userRegistrationRequestContent() )
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
                        .content( userRegistrationRequestContent() )
                        .contentType(MediaType.APPLICATION_JSON) );

        // Then
        result.andExpect(status().isBadRequest());
    }

    // ------------------------------------------------------------------------

    private String userRegistrationRequestContent() throws Exception{

        return mapper.writeValueAsString(
                new UserRegistrationDto(USER_EMAIL,USER_PASSWORD));
    }

    private RegisteredUserDto aRegisteredUser() {
        return new RegisteredUserDto(10L, 20L);
    }
}
