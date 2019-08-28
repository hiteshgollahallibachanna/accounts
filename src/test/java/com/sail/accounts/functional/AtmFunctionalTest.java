package com.sail.accounts.functional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sail.accounts.model.Account;
import com.sail.accounts.model.AccountDto;
import com.sail.accounts.model.Transaction;
import com.sail.accounts.commons.model.ErrorResponse;
import com.sail.accounts.util.BaseTest;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AtmFunctionalTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(AtmFunctionalTest.class);

    private static TestRestTemplate restTemplate = null;

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private ObjectMapper objectMapper;

    @Before
    public void init() {
        restTemplate = new TestRestTemplate();
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/accounts/v1");
        objectMapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        objectMapper.registerModules(module);
    }


    @Test
    public void loginAndPerformDepositsAndWithdrawAndLogout() throws JsonProcessingException {
        //login
        String loginUrl = baseUrl.concat("/login");
        Account account = new Account();
        account.setPan("111-222");
        account.setPin(1234);
        AccountDto expectedAccount = restTemplate.postForObject(loginUrl, account, AccountDto.class);
        assertTrue(expectedAccount.isSignedIn());

        //deposit
        String depositUrl = baseUrl.concat("/deposit");
        Transaction deposit = new Transaction();
        deposit.setAmount(new BigDecimal(50));
        deposit.setPan("111-222");

        Transaction depositOutput = restTemplate.postForObject(depositUrl, deposit, Transaction.class);

        Transaction depositOutput1 = restTemplate.postForObject(depositUrl, deposit, Transaction.class);
        Transaction depositOutput2 = restTemplate.postForObject(depositUrl, deposit, Transaction.class);

        assertNotNull(depositOutput);
        assertNotNull(depositOutput1);
        assertNotNull(depositOutput2);
        assertEquals(new BigDecimal(50), depositOutput.getAmount());
        assertEquals(new BigDecimal(50), depositOutput1.getAmount());
        assertEquals(new BigDecimal(50), depositOutput2.getAmount());

        //withdraw
        String withdrawUrl = baseUrl.concat("/withdraw");
        Transaction withdraw = new Transaction();
        withdraw.setAmount(new BigDecimal(50));
        withdraw.setPan("111-222");

        Transaction withdrawOutput = restTemplate.postForObject(withdrawUrl, withdraw, Transaction.class);

        assertNotNull(withdrawOutput);
        assertEquals(new BigDecimal(50), withdrawOutput.getAmount());

        //view balance
        String viewBalanceUrl = baseUrl.concat("/111-222");
        ResponseEntity<Account> viewBalanceOutput = restTemplate.getForEntity(viewBalanceUrl, Account.class);
        assertEquals(new BigDecimal(100), Objects.requireNonNull(viewBalanceOutput.getBody()).getBalance());

        //logout
        String logoutUrl = baseUrl.concat("/logout/111-222");
        ResponseEntity<String> forEntity = restTemplate.getForEntity(logoutUrl, String.class);
        assertEquals(HttpStatus.OK, forEntity.getStatusCode());
    }

    @Test
    public void validationErrorsOnLogin() {
        String loginUrl = baseUrl.concat("/login");
        Account account = new Account();
        account.setPan("111-222");
        HttpEntity<Account> entity = new HttpEntity<Account>(account, new HttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange(loginUrl, HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    public void validationErrorsOnDeposit() throws IOException, JSONException {
        String loginUrl = baseUrl.concat("/login");
        Account account = new Account();
        account.setPan("111-222");
        account.setPin(1234);
        AccountDto expectedAccount = restTemplate.postForObject(loginUrl, account, AccountDto.class);
        assertTrue(expectedAccount.isSignedIn());

        String depositUrl = baseUrl.concat("/deposit");
        Transaction deposit = new Transaction();
        deposit.setAmount(new BigDecimal(50));

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(depositUrl, deposit, ErrorResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        String errorResponse = getJsonData("deposit-error-response.json");
        String actualJson = objectMapper.writeValueAsString(response.getBody());
        JSONAssert.assertEquals(errorResponse, actualJson, false);
    }

    @Test
    public void invalidDespositWithoutLogin(){
        //deposit without login
        String depositUrl = baseUrl.concat("/deposit");
        Transaction deposit = new Transaction();
        deposit.setAmount(new BigDecimal(50));
        deposit.setPan("333-444");

        ResponseEntity<Transaction> response = restTemplate.postForEntity(depositUrl, deposit, Transaction.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void invalidWithDrawWithoutLogin(){
        //withdraw without login
        String depositUrl = baseUrl.concat("/withdraw");
        Transaction deposit = new Transaction();
        deposit.setAmount(new BigDecimal(50));
        deposit.setPan("333-444");

        ResponseEntity<Transaction> response = restTemplate.postForEntity(depositUrl, deposit, Transaction.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
