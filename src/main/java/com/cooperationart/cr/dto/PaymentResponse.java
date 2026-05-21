package com.cooperationart.cr.dto;

import com.cooperationart.cr.domain.PaymentStatus;

public class PaymentResponse {
    private final String transactionId;
    private final PaymentStatus status;
    private final String message;

    public PaymentResponse(String transactionId, PaymentStatus status, String message) {
        this.transactionId = transactionId;
        this.status = status;
        this.message = message;
    }

    public String getTransactionId() { return transactionId; }
    public PaymentStatus getStatus() { return status; }
    public String getMessage() { return message; }
}
