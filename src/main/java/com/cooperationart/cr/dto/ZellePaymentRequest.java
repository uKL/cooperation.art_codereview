package com.cooperationart.cr.dto;
 
import com.cooperationart.cr.domain.PaymentMethod;
import java.math.BigDecimal;
 
/**
 * Request payload for Zelle payments.
 * Business rules:
 * - Transaction amount must be strictly positive.
 * - Single transaction amount must not exceed the maximum limit of $500.00.
 */
public class ZellePaymentRequest extends BasePaymentRequest {
    private final String recipientEmail;

    public ZellePaymentRequest(String sourceAccountNumber, String recipientEmail, BigDecimal amount) {
        super(sourceAccountNumber, amount, PaymentMethod.ZELLE);
        this.recipientEmail = recipientEmail;
    }

    public String getRecipientEmail() { return recipientEmail; }
}
