package com.cooperationart.cr.service;

import com.cooperationart.cr.domain.*;
import com.cooperationart.cr.dto.*;
import com.cooperationart.cr.config.ZelleGatewayConfig;
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
    private ZellePaymentGateway zellePaymentGateway;

    @BeforeEach
    public void setUp() {
        accountRepository = new MockAccountRepository();
        transactionRepository = new MockTransactionRepository();
        auditLogger = new MockAuditLogger();
        accountService = new AccountService(accountRepository);
        
        ZelleGatewayConfig zelleConfig = new ZelleGatewayConfig("https://api.zellepay.com/v1", "key", 5000);
        zellePaymentGateway = new ZellePaymentGateway(zelleConfig);
        
        paymentProcessor = new PaymentProcessor(accountService, transactionRepository, auditLogger, zellePaymentGateway);

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
    public void testSuccessfulZelleTransfer() {
        ZellePaymentRequest request = new ZellePaymentRequest("123", "client@example.com", new BigDecimal("200.00"));
        PaymentResponse response = paymentProcessor.processPayment(request);

        assertEquals(PaymentStatus.SUCCESS, response.getStatus());
        assertEquals(new BigDecimal("800.00"), accountService.getAccount("123").getBalance());
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
