package com.sail.accounts.service.transaction;

import com.sail.accounts.model.Transaction;

import java.math.BigDecimal;

public interface TransactionService {
    Transaction withdraw(String pan, BigDecimal amount);

    Transaction deposit(String pan, BigDecimal amount);
}
