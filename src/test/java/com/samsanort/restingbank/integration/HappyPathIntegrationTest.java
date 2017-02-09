package com.samsanort.restingbank.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

/**
 * TODO Add description
 */

@RunWith(SpringRunner.class)
@WebMvcTest
public class HappyPathIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void happyPathJourney() {

        // register a user

        // get its id

        // make a deposit

        // make a withdrawal

        // get its account id

        // get an statement

    }

    private void registerUser() {
        // TODO
    }

    private Long fetchUserId() {
        // TODO
        return null;
    }

    private Long fetchAccountId() {
        // TODO
        return null;
    }

    private void depositMoney(BigDecimal amount) {
        // TODO
    }

    private void withdrawMoney(BigDecimal amount) {
        // TODO
    }
}
