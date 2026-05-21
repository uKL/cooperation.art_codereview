package com.cooperationart.cr.dto;

import com.cooperationart.cr.domain.PaymentMethod;
import java.math.BigDecimal;

public class InternalPaymentRequest extends BasePaymentRequest {
    private final String targetAccountNumber;

    public InternalPaymentRequest(String sourceAccountNumber, String targetAccountNumber, BigDecimal amount) {
        super(sourceAccountNumber, amount, PaymentMethod.INTERNAL);
        this.targetAccountNumber = targetAccountNumber;
    }

    public String getTargetAccountNumber() { return targetAccountNumber; }
}
