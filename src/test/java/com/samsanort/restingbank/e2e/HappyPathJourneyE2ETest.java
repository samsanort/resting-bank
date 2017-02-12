package com.samsanort.restingbank.e2e;

import com.samsanort.restingbank.model.dto.RegisteredUserDto;
import com.samsanort.restingbank.model.dto.StatementDto;
import com.samsanort.restingbank.model.dto.UserRegistrationDto;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.Charset;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;

/**
 * End to End test for a happy path journey:
 *
 * - User registration
 * - Perform deposits and withdrawals
 * - Obtain the bank account statement
 *
 * [!] Prerequisites for running this test:
 * The application is up and is a fresh run - otherwise the user to register
 * will already exist in the database.
 */

public class HappyPathJourneyE2ETest {

    private RestTemplate restTemplate = new RestTemplate();

    private static final String BASE_URL = "http://localhost:8080";

    /**
     * E2E Test run method.
     * @param args - N/A
     * @throws Exception if anything goes wrong.
     */
    public static void main(String[] args) throws Exception {

        try {
            HappyPathJourneyE2ETest test = new HappyPathJourneyE2ETest();
            test.runHappyPathJourney();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void runHappyPathJourney() {

        // GIVEN - a user to register
        UserRegistrationDto registrationInfo = aUserRegistration();

        // WHEN - the user is registered
        RegisteredUserDto registeredUser = registerNewUser(registrationInfo);
        assertNotNull(registeredUser);

        // AND WHEN - some transactions are performed on the user's account
        makeDeposit( registrationInfo, registeredUser.getAccountId(), new BigDecimal(50));
        makeWithdrawal( registrationInfo, registeredUser.getAccountId(), new BigDecimal(20));
        makeDeposit( registrationInfo, registeredUser.getAccountId(), new BigDecimal(70));

        // AND WHEN - the statement for the user's account is obtained
        StatementDto statement = obtainStatement(registrationInfo, registeredUser.getAccountId());

        // THEN - the obtained statement contains the transactions performed on the account
        assertThat( statement.getTransactions().get(0).getAmount().intValue(), is( equalTo( new BigDecimal(50).intValue() )));
        assertThat( statement.getTransactions().get(0).getDescription(), is( equalTo( "Deposit" )));
        assertThat( statement.getTransactions().get(1).getAmount().intValue(), is( equalTo( new BigDecimal(20).intValue() )));
        assertThat( statement.getTransactions().get(1).getDescription(), is( equalTo( "Withdrawal" )));
        assertThat( statement.getTransactions().get(2).getAmount().intValue(), is( equalTo( new BigDecimal(70).intValue() )));
        assertThat( statement.getTransactions().get(2).getDescription(), is( equalTo( "Deposit" )));

        // AND THEN - the balance reflects the transactions performed
        assertThat( statement.getBalance().intValue(), is( equalTo( new BigDecimal(100).intValue() )));

        System.out.println("All looks good! :)");
    }

    private RegisteredUserDto registerNewUser(UserRegistrationDto userRegistration) {

        return restTemplate.postForObject(
                BASE_URL + "/users",
                userRegistration,
                RegisteredUserDto.class);
    }

    private void makeDeposit(UserRegistrationDto user, Long accountId, BigDecimal amount) {

        String url = String.format("%s/accounts/%s/deposits", BASE_URL, accountId);
        HttpEntity<BigDecimal> request = new HttpEntity<>(amount, generateAuthHeader(user));
        restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
    }

    private void makeWithdrawal(UserRegistrationDto user, Long accountId, BigDecimal amount) {

        String url = String.format("%s/accounts/%s/withdrawals", BASE_URL, accountId);
        HttpEntity<BigDecimal> request = new HttpEntity<>(amount, generateAuthHeader(user));
        restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
    }

    private StatementDto obtainStatement(UserRegistrationDto user, Long accountId) {

        String url = String.format("%s/accounts/%s", BASE_URL, accountId);
        HttpEntity<BigDecimal> request = new HttpEntity<>(generateAuthHeader(user));
        ResponseEntity<StatementDto> response = restTemplate.exchange(url, HttpMethod.GET, request, StatementDto.class);
        return response.getBody();
    }

    private UserRegistrationDto aUserRegistration() {

        return new UserRegistrationDto(
                "user1@mail.com",
                "password");
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
