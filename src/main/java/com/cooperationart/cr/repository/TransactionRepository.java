package com.cooperationart.cr.repository;

import com.cooperationart.cr.domain.Transaction;

public interface TransactionRepository {
    void save(Transaction transaction);
}
