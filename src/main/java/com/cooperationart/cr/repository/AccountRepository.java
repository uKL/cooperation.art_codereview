package com.cooperationart.cr.repository;

import com.cooperationart.cr.domain.Account;
import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findByAccountNumber(String accountNumber);
    void save(Account account);
}
