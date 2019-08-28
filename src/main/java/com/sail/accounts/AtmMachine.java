package com.sail.accounts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sail.accounts.commons.model.ErrorResponse;
import com.sail.accounts.model.Account;
import com.sail.accounts.model.AccountDto;
import com.sail.accounts.model.Transaction;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Scanner;

public class AtmMachine {
    static ObjectMapper objectMapper = new ObjectMapper();
    static Scanner scanner = new Scanner(System.in);
    static int choice;
    static AccountDto accountDto = null;

    public static void main(String[] args) throws JsonProcessingException {
        String baseUrl = "http://localhost:8080/accounts/v1";
        RestTemplate restTemplate = new RestTemplate();

        do {
            if (accountDto == null || !accountDto.isSignedIn()) {
                accountDto = login(baseUrl, restTemplate);
            }

            if (accountDto != null && accountDto.isSignedIn()) {
                choiceSelection();
                executeChoice(baseUrl, restTemplate, accountDto.getPan());
            }

        } while (choice != 6);
    }

    private static AccountDto login(String baseUrl, RestTemplate restTemplate) throws JsonProcessingException {
        try {
            System.out.println("Enter credit card");
            String pan = scanner.nextLine();

            System.out.println("Enter 4 digit pin");
            String pin = scanner.nextLine();

            String loginUrl = baseUrl.concat("/login");
            Account account = new Account();
            account.setPan(pan);
            account.setPin(Integer.valueOf(pin));
            return restTemplate.postForObject(loginUrl, account, AccountDto.class);
        } catch (NumberFormatException e) {
            System.out.println("input not valid");
        } catch (HttpClientErrorException e) {
            handleHttpClientException(e);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static void executeChoice(String baseUrl, RestTemplate restTemplate, String pan) throws JsonProcessingException {

        switch (choice) {
            case 1:
                try {
                    System.out.println("Enter amount to withdraw");
                    String amount = scanner.nextLine();
                    String withdrawUrl = baseUrl.concat("/withdraw");
                    Transaction withdraw = new Transaction();
                    withdraw.setAmount(new BigDecimal(amount));
                    withdraw.setPan(pan);
                    Transaction transaction = restTemplate.postForObject(withdrawUrl, withdraw, Transaction.class);
                    System.out.println("Withdraw successful :" + transaction.getAmount());
                } catch (HttpClientErrorException e) {
                    handleHttpClientException(e);
                } catch (NumberFormatException e) {
                    System.out.println("input not valid");
                } catch (Exception e) {
                    String message = e.getMessage() == null ? "input not valid" : e.getMessage();
                    System.out.println(message);
                }
                break;

            case 2:
                try {
                    System.out.println("Enter amount to deposit");
                    String amount = scanner.nextLine();
                    String depositUrl = baseUrl.concat("/deposit");
                    Transaction deposit = new Transaction();
                    deposit.setAmount(new BigDecimal(amount));
                    deposit.setPan(pan);

                    Transaction transaction = restTemplate.postForObject(depositUrl, deposit, Transaction.class);
                    System.out.println("Deposit successful :" + transaction.getAmount());
                } catch (HttpClientErrorException e) {
                    handleHttpClientException(e);
                } catch (NumberFormatException e) {
                    System.out.println("input not valid");
                } catch (Exception e) {
                    String message = e.getMessage() == null ? "input not valid" : e.getMessage();
                    System.out.println(message);

                }

                break;
            case 3:
                try {
                    String viewBalanceUrl = baseUrl.concat("/" + pan);
                    Account account = restTemplate.getForObject(viewBalanceUrl, Account.class);
                    System.out.println("Current balance :" + account.getBalance());
                } catch (HttpClientErrorException e) {
                    handleHttpClientException(e);
                } catch (Exception e) {
                    String message = e.getMessage() == null ? "input not valid" : e.getMessage();
                    System.out.println(message);

                }
                break;
            case 4:
                try {
                    String logoutUrl = baseUrl.concat("/logout/" + pan);
                    restTemplate.getForObject(logoutUrl, String.class);
                    System.out.println("Logout successful");
                } catch (HttpClientErrorException e) {
                    handleHttpClientException(e);
                } catch (Exception e) {
                    String message = e.getMessage() == null ? "input not valid" : e.getMessage();
                    System.out.println(message);

                }
                break;
            case 5:
                accountDto = login(baseUrl, restTemplate);
                break;
        }
    }

    private static void handleHttpClientException(HttpClientErrorException e) throws JsonProcessingException {
        if (e.getRawStatusCode() == 401) {
            System.out.println("pan or pin does not match");
        } else {
            ErrorResponse errorResponse = objectMapper.readValue(e.getResponseBodyAsString(), ErrorResponse.class);
            System.out.println(errorResponse.getMessage());
        }
    }

    private static void choiceSelection() {
        try {
            System.out.println("\n");
            System.out.println("Enter your choice");
            System.out.println("**************************************************");
            System.out.println("List of option available");
            System.out.println("Press 1 to withdraw");
            System.out.println("Press 2 to deposit");
            System.out.println("Press 3 to view current balance");
            System.out.println("Press 4 to logout");
            System.out.println("Press 5 to login");
            System.out.println("Press 6 to exit");
            System.out.println("**************************************************");

            String c = scanner.nextLine().trim();
            choice = Integer.parseInt(c);

            if (choice > 6) {
                System.out.println("Invalid choice selected");
            }
        } catch (Exception e) {
            System.out.println("Invalid choice selected");
        }
    }
}
