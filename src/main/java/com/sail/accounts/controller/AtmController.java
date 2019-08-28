package com.sail.accounts.controller;

import com.sail.accounts.commons.controller.BaseController;
import com.sail.accounts.commons.util.LoginGroup;
import com.sail.accounts.model.Account;
import com.sail.accounts.model.AccountDto;
import com.sail.accounts.model.Transaction;
import com.sail.accounts.service.account.AccountService;
import com.sail.accounts.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;

@RestController
@RequestMapping(value = "/accounts/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class AtmController extends BaseController {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final Validator validator;

    @Autowired
    public AtmController(AccountService accountService, TransactionService transactionService, Validator validator) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.validator = validator;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Account account) {

        Set<ConstraintViolation<Account>> validate = validator.validate(account, LoginGroup.class, Default.class);
        if (!validate.isEmpty()) {
            throw new ConstraintViolationException(validate);
        }

        AccountDto login = accountService.login(account.getPan(), account.getPin());
        return createResponse(login, HttpStatus.CREATED);
    }

    @GetMapping("/logout/{pan}")
    public ResponseEntity<Object> logout(@PathVariable String pan) {
        accountService.logout(pan);
        return createResponse(HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Object> withdraw(@RequestBody Transaction transaction) {
        Set<ConstraintViolation<Transaction>> validate = validator.validate(transaction, Default.class);
        if (!validate.isEmpty()) {
            throw new ConstraintViolationException(validate);
        }

        return createResponse(transactionService.withdraw(transaction.getPan(), transaction.getAmount()), HttpStatus.CREATED);
    }

    @PostMapping("/deposit")
    public ResponseEntity<Object> deposit(@RequestBody Transaction transaction) {
        Set<ConstraintViolation<Transaction>> validate = validator.validate(transaction, Default.class);
        if (!validate.isEmpty()) {
            throw new ConstraintViolationException(validate);
        }

        return createResponse(transactionService.deposit(transaction.getPan(), transaction.getAmount()), HttpStatus.CREATED);
    }

    @GetMapping("/{pan}")
    public ResponseEntity<Object> view(@PathVariable String pan) {
        return createResponse(accountService.read(pan), HttpStatus.OK);
    }
}
