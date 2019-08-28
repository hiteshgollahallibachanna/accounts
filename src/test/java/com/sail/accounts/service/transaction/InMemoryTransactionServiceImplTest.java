package com.sail.accounts.service.transaction;

import com.sail.accounts.commons.exceptions.UnAuthorizedException;
import com.sail.accounts.commons.exceptions.ValidationException;
import com.sail.accounts.model.Transaction;
import com.sail.accounts.repository.AccountRepository;
import com.sail.accounts.service.account.AccountService;
import com.sail.accounts.service.account.InMemoryAccountServiceImpl;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class InMemoryTransactionServiceImplTest {
    AccountRepository accountRepository = new AccountRepository();
    AccountService accountService = new InMemoryAccountServiceImpl(accountRepository);
    TransactionService TransactionService =  new InMemoryTransactionServiceImpl(accountService);

    @Test
    public void depositMoney() {
        accountService.login("111-222", 1234);
        Transaction deposit = TransactionService.deposit("111-222", new BigDecimal(50));
        assertEquals(new BigDecimal(50), deposit.getAmount());
    }

    @Test(expected = UnAuthorizedException.class)
    public void cantDepositMoneyWithoutLogin() {
        TransactionService.deposit("111-222", new BigDecimal(50));
    }

    @Test
    public void withdrawMoney() {
        accountService.login("111-222", 1234);
        TransactionService.deposit("111-222", new BigDecimal(50));
        Transaction withdraw = TransactionService.withdraw("111-222", new BigDecimal(50));
        assertEquals(new BigDecimal(50), withdraw.getAmount());
    }

    @Test(expected = ValidationException.class)
    public void cantWithdrawMoneyYouDontHave() {
        accountService.login("111-222", 1234);
        Transaction withdraw = TransactionService.withdraw("111-222", new BigDecimal(50));
        assertEquals(new BigDecimal(50), withdraw.getAmount());
    }

    @Test(expected = UnAuthorizedException.class)
    public void cantWithdrawWithoutLogin() {
        TransactionService.withdraw("111-222", new BigDecimal(50));
    }


}
