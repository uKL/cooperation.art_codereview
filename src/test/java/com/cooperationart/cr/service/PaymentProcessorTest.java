package com.cooperationart.cr.service;

import com.cooperationart.cr.domain.*;
import com.cooperationart.cr.dto.*;
import com.cooperationart.cr.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentProcessorTest {
    private PaymentProcessor paymentProcessor;
    private AccountService accountService;
    private MockAccountRepository accountRepository;
    private MockTransactionRepository transactionRepository;
    private MockAuditLogger auditLogger;

    @BeforeEach
    public void setUp() {
        accountRepository = new MockAccountRepository();
        transactionRepository = new MockTransactionRepository();
        auditLogger = new MockAuditLogger();
        accountService = new AccountService(accountRepository);
        paymentProcessor = new PaymentProcessor(accountService, transactionRepository, auditLogger);

        accountRepository.save(new Account("123", new BigDecimal("1000.00"), "Paweł"));
        accountRepository.save(new Account("456", new BigDecimal("500.00"), "Jaga"));
    }

    @Test
    public void testSuccessfulInternalTransfer() {
        InternalPaymentRequest request = new InternalPaymentRequest("123", "456", new BigDecimal("200.00"));
        PaymentResponse response = paymentProcessor.processPayment(request);

        assertEquals(PaymentStatus.SUCCESS, response.getStatus());
        assertEquals(new BigDecimal("800.00"), accountService.getAccount("123").getBalance());
        assertEquals(new BigDecimal("700.00"), accountService.getAccount("456").getBalance());
    }

    @Test
    public void testInsufficientFundsInternalTransfer() {
        InternalPaymentRequest request = new InternalPaymentRequest("123", "456", new BigDecimal("1500.00"));
        PaymentResponse response = paymentProcessor.processPayment(request);

        assertEquals(PaymentStatus.FAILED, response.getStatus());
        assertTrue(response.getMessage().contains("Insufficient funds"));
    }

    @Test
    public void testNegativeAmountTransfer() {
        InternalPaymentRequest request = new InternalPaymentRequest("123", "456", new BigDecimal("-50.00"));
        assertThrows(RuntimeException.class, () -> {
            paymentProcessor.processPayment(request);
        });
    }

    private static class MockAccountRepository implements AccountRepository {
        private final java.util.Map<String, Account> accounts = new java.util.HashMap<>();

        @Override
        public Optional<Account> findByAccountNumber(String accountNumber) {
            return Optional.ofNullable(accounts.get(accountNumber));
        }

        @Override
        public void save(Account account) {
            accounts.put(account.getAccountNumber(), account);
        }
    }

    private static class MockTransactionRepository implements TransactionRepository {
        @Override
        public void save(Transaction transaction) {}
    }

    private static class MockAuditLogger implements AuditLogger {
        @Override
        public void logTransaction(String id, String src, String dst, BigDecimal amt, String type) {}
    }
}
