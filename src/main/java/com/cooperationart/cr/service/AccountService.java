package com.cooperationart.cr.service;

import com.cooperationart.cr.domain.Account;
import com.cooperationart.cr.exception.AccountNotFoundException;
import com.cooperationart.cr.repository.AccountRepository;
import java.math.BigDecimal;

public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
    }

    public void transferFunds(String sourceAccount, String targetAccount, BigDecimal amount) {
        Account source = getAccount(sourceAccount);
        Account target = getAccount(targetAccount);
        source.debit(amount);
        target.credit(amount);
        accountRepository.save(source);
        accountRepository.save(target);
    }

    public void debitAccount(String accountNumber, BigDecimal amount) {
        Account account = getAccount(accountNumber);
        account.debit(amount);
        accountRepository.save(account);
    }
}
