package com.sail.accounts.service.transaction;

import com.sail.accounts.commons.exceptions.UnAuthorizedException;
import com.sail.accounts.commons.exceptions.ValidationException;
import com.sail.accounts.commons.util.TransactionType;
import com.sail.accounts.model.Account;
import com.sail.accounts.model.Transaction;
import com.sail.accounts.service.account.AccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
public class InMemoryTransactionServiceImpl implements TransactionService {


    private final AccountService accountService;


    public InMemoryTransactionServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }


    @Override
    public Transaction withdraw(String pan, BigDecimal amount) {
        Account account = accountService.read(pan);
        if (account.isSignedIn()) {
            BigDecimal newBalance = account.getBalance().subtract(amount);
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new ValidationException("not enough money");
            }

            //create a single transaction
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setType(TransactionType.WITHDRAW.name());
            transaction.setPan(pan);
            transaction.setCreated(OffsetDateTime.now());

            //add the transaction to the list of transaction foe the account
            account.getTransactions().add(transaction);

            //update the balance
            account.setBalance(newBalance);
            return transaction;

        } else {
            throw new UnAuthorizedException("Not logged in");
        }
    }

    @Override
    public Transaction deposit(String pan, BigDecimal amount) {
        Account read = accountService.read(pan);
        if (read.isSignedIn()) {
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setType(TransactionType.DEPOSIT.name());
            transaction.setPan(pan);
            transaction.setCreated(OffsetDateTime.now());

            //keep track of the list of transaction
            read.getTransactions().add(transaction);
            BigDecimal newBalance = read.getBalance().add(amount);

            //update the balance
            read.setBalance(newBalance);

            return transaction;
        } else {
            throw new UnAuthorizedException("Not logged in");
        }
    }
}
