package com.sail.accounts.service.account;

import com.sail.accounts.commons.exceptions.UnAuthorizedException;
import com.sail.accounts.model.Account;
import com.sail.accounts.model.AccountDto;
import com.sail.accounts.repository.AccountRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.Assert.assertNotNull;

public class InMemoryAccountServiceImplTest {
    AccountRepository accountRepository = new AccountRepository();
    AccountService accountService = new InMemoryAccountServiceImpl(accountRepository);

    @Test
    public void beAbleLogin() {
        AccountDto login = accountService.login("111-222", 1234);
        Assertions.assertTrue(login.isSignedIn());
    }

    @Test(expected = UnAuthorizedException.class)
    public void loginFailsForInValidPan() {
        accountService.login("abc", 1234);
    }

    @Test
    public void beAbleLogout() {
        accountService.login("111-222", 1234);
        accountService.logout("111-222");

        Account account = accountRepository.read("111-222");
        Assertions.assertFalse(account.isSignedIn());
    }

    @Test
    public void ableToViewBalance() {
        accountService.login("111-222", 1234);

        Account account = accountService.read("111-222");
        assertNotNull(account);
    }


    @Test(expected = UnAuthorizedException.class)
    public void cannotViewBalanceWithoutLogin() {
        Account account = accountService.read("111-222");
        assertNotNull(account);
    }

}
