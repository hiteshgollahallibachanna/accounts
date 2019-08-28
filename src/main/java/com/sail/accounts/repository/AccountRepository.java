package com.sail.accounts.repository;

import com.sail.accounts.model.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class AccountRepository {
    Map<String, Account> accounts = new HashMap<>();

    public AccountRepository() {
        accounts.put("111-222", new Account("foo", "bar", "111-222", 1234, false, BigDecimal.ZERO, new ArrayList<>()));
        accounts.put("333-444", new Account("jack", "ryan", "333-444", 9090, false, BigDecimal.ZERO, new ArrayList<>()));
    }

    public Account read(String pan) {
        return accounts.get(pan);
    }

}
