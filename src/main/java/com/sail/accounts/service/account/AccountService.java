package com.sail.accounts.service.account;


import com.sail.accounts.model.Account;
import com.sail.accounts.model.AccountDto;

public interface AccountService {
    AccountDto login(String pan, int pin);

    void logout(String pan);

    Account read(String pan);
}
