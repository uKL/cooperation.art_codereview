package com.cooperationart.cr.service;

import com.cooperationart.cr.domain.*;
import com.cooperationart.cr.dto.*;
import com.cooperationart.cr.exception.*;
import com.cooperationart.cr.repository.*;
import java.math.BigDecimal;
import java.util.UUID;

public class PaymentProcessor {
    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final AuditLogger auditLogger;
    private final ZellePaymentGateway zellePaymentGateway;

    public PaymentProcessor(AccountService accountService, TransactionRepository transactionRepository, AuditLogger auditLogger, ZellePaymentGateway zellePaymentGateway) {
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
        this.auditLogger = auditLogger;
        this.zellePaymentGateway = zellePaymentGateway;
    }

    public PaymentResponse processPayment(BasePaymentRequest request) {
        if (request == null) {
            throw new InvalidPaymentException("Payment request cannot be null");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPaymentException("Payment amount must be greater than zero");
        }

        String transactionId = UUID.randomUUID().toString();

        try {
            if (request.getMethod() == PaymentMethod.INTERNAL) {
                InternalPaymentRequest internalReq = (InternalPaymentRequest) request;
                Account source = accountService.getAccount(internalReq.getSourceAccountNumber());
                
                if (source.getBalance().compareTo(internalReq.getAmount()) < 0) {
                    throw new InsufficientFundsException("Insufficient funds in account: " + internalReq.getSourceAccountNumber());
                }

                accountService.transferFunds(
                    internalReq.getSourceAccountNumber(),
                    internalReq.getTargetAccountNumber(),
                    internalReq.getAmount()
                );

                Transaction tx = new Transaction(
                    transactionId,
                    internalReq.getSourceAccountNumber(),
                    internalReq.getTargetAccountNumber(),
                    internalReq.getAmount(),
                    PaymentStatus.SUCCESS,
                    PaymentMethod.INTERNAL
                );
                transactionRepository.save(tx);
                auditLogger.logTransaction(transactionId, internalReq.getSourceAccountNumber(), internalReq.getTargetAccountNumber(), internalReq.getAmount(), "INTERNAL");

                return new PaymentResponse(transactionId, PaymentStatus.SUCCESS, "Payment processed successfully");
            } else if (request.getMethod() == PaymentMethod.ZELLE) {
                ZellePaymentRequest zelleReq = (ZellePaymentRequest) request;
                Account source = accountService.getAccount(zelleReq.getSourceAccountNumber());
                
                if (source.getBalance().compareTo(zelleReq.getAmount()) < 0) {
                    throw new InsufficientFundsException("Insufficient funds in account: " + zelleReq.getSourceAccountNumber());
                }

                // Call external Zelle gateway
                zellePaymentGateway.sendPayment(zelleReq);

                // Perform local debit
                accountService.debitAccount(zelleReq.getSourceAccountNumber(), zelleReq.getAmount());

                Transaction tx = new Transaction(
                    transactionId,
                    zelleReq.getSourceAccountNumber(),
                    zelleReq.getRecipientEmail(),
                    zelleReq.getAmount(),
                    PaymentStatus.SUCCESS,
                    PaymentMethod.ZELLE
                );
                transactionRepository.save(tx);
                auditLogger.logTransaction(transactionId, zelleReq.getSourceAccountNumber(), zelleReq.getRecipientEmail(), zelleReq.getAmount(), "ZELLE");

                return new PaymentResponse(transactionId, PaymentStatus.SUCCESS, "Zelle payment processed successfully");
            } else {
                throw new UnsupportedOperationException("Payment method not supported: " + request.getMethod());
            }
        } catch (Exception e) {
            Transaction tx = new Transaction(
                transactionId,
                request.getSourceAccountNumber(),
                null,
                request.getAmount(),
                PaymentStatus.FAILED,
                request.getMethod()
            );
            transactionRepository.save(tx);
            return new PaymentResponse(transactionId, PaymentStatus.FAILED, e.getMessage());
        }
    }
}
