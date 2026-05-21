package com.cooperationart.cr.dto;

import com.cooperationart.cr.domain.PaymentMethod;
import java.math.BigDecimal;

public abstract class BasePaymentRequest {
    private final String sourceAccountNumber;
    private final BigDecimal amount;
    private final PaymentMethod method;

    protected BasePaymentRequest(String sourceAccountNumber, BigDecimal amount, PaymentMethod method) {
        this.sourceAccountNumber = sourceAccountNumber;
        this.amount = amount;
        this.method = method;
    }

    public String getSourceAccountNumber() { return sourceAccountNumber; }
    public BigDecimal getAmount() { return amount; }
    public PaymentMethod getMethod() { return method; }
}
