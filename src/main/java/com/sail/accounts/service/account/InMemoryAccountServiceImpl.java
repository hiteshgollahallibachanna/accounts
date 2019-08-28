package com.sail.accounts.service.account;


import com.sail.accounts.commons.exceptions.UnAuthorizedException;
import com.sail.accounts.model.Account;
import com.sail.accounts.model.AccountDto;
import com.sail.accounts.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class InMemoryAccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public InMemoryAccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto login(String pan, int pin) {
        Account account = accountRepository.read(pan);
        if (account != null && account.getPin() == pin) {
            account.setSignedIn(true);
            AccountDto accountDto = new AccountDto();
            accountDto.setPan(account.getPan());
            accountDto.setFirstName(account.getFirstName());
            accountDto.setLastName(account.getLastName());
            accountDto.setSignedIn(account.isSignedIn());
            return accountDto;
        } else {
            throw new UnAuthorizedException("pin or pan mismatch");
        }
    }

    @Override
    public void logout(String pan) {
        Account account = accountRepository.read(pan);
        if (account != null && account.isSignedIn()) {
            account.setSignedIn(false);
        } else {
            throw new UnAuthorizedException("pin or pan mismatch");
        }
    }

    @Override
    public Account read(String pan) {
        Account account = accountRepository.read(pan);
        if (account != null && account.isSignedIn()) {
            return account;
        } else {
            throw new UnAuthorizedException("pin or pan mismatch");
        }
    }
}
