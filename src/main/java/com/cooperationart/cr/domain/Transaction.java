package com.cooperationart.cr.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private final String id;
    private final String sourceAccount;
    private final String targetAccount;
    private final BigDecimal amount;
    private final LocalDateTime timestamp;
    private final PaymentStatus status;
    private final PaymentMethod method;

    public Transaction(String id, String sourceAccount, String targetAccount, BigDecimal amount, PaymentStatus status, PaymentMethod method) {
        this.id = id;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.method = method;
    }

    public String getId() { return id; }
    public String getSourceAccount() { return sourceAccount; }
    public String getTargetAccount() { return targetAccount; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public PaymentStatus getStatus() { return status; }
    public PaymentMethod getMethod() { return method; }
}
