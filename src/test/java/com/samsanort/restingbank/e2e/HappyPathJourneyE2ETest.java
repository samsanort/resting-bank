package com.samsanort.restingbank.e2e;

import com.samsanort.restingbank.model.dto.RegisteredUserDto;
import com.samsanort.restingbank.model.dto.StatementDto;
import com.samsanort.restingbank.model.dto.UserRegistrationDto;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.Charset;

import static org.junit.Assert.assertNotNull;

/**
 * TODO describe
 */

public class HappyPathJourneyE2ETest {

    RestTemplate restTemplate = new RestTemplate();

    private static final String BASE_URL = "http://localhost:8080";

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        try {
            HappyPathJourneyE2ETest test = new HappyPathJourneyE2ETest();
            test.runHappyPathJourney();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    void runHappyPathJourney() {

        UserRegistrationDto registrationInfo = aUserRegistration();

        // register a new user
        RegisteredUserDto registeredUser = registerNewUser(registrationInfo);
        assertNotNull(registeredUser);

        // make some transactions
        makeDeposit(registrationInfo, registeredUser.getAccountId(), new BigDecimal(50));
        makeWithdrawal(registrationInfo, registeredUser.getAccountId(), new BigDecimal(20));
        makeDeposit(registrationInfo, registeredUser.getAccountId(), new BigDecimal(70));

        // obtain the statement
        StatementDto statement = obtainStatement(registrationInfo, registeredUser.getAccountId());

        System.out.println(statement);
    }

    private RegisteredUserDto registerNewUser(UserRegistrationDto userRegistration) {
        return restTemplate.postForObject(BASE_URL + "/users", userRegistration, RegisteredUserDto.class);
    }

    private void makeDeposit(UserRegistrationDto user, Long accountId, BigDecimal amount) {
        String url = String.format("%s/accounts/%s/deposits", BASE_URL, accountId);
        HttpEntity<BigDecimal> request = new HttpEntity<>(generateAuthHeader(user));
        restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
    }

    private void makeWithdrawal(UserRegistrationDto user, Long accountId, BigDecimal amount) {
        String url = String.format("%s/accounts/%s/withdrawals", BASE_URL, accountId);
        HttpEntity<BigDecimal> request = new HttpEntity<>(generateAuthHeader(user));
        restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
    }

    private StatementDto obtainStatement(UserRegistrationDto user, Long accountId) {
        String url = String.format("%s/accounts/%s", BASE_URL, accountId);
        HttpEntity<BigDecimal> request = new HttpEntity<>(generateAuthHeader(user));
        ResponseEntity<StatementDto> response = restTemplate.exchange(url, HttpMethod.GET, request, StatementDto.class);
        return response.getBody();
    }

    private UserRegistrationDto aUserRegistration() {
        return new UserRegistrationDto("user1@mail.com", "password");
    }

    private HttpHeaders generateAuthHeader(UserRegistrationDto registration){

        HttpHeaders headers = new HttpHeaders();

        String auth = registration.getEmail() + ":" + registration.getPassword();
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String( encodedAuth );

        headers.set("Authorization", authHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

}
