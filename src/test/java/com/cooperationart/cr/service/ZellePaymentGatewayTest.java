package com.cooperationart.cr.service;

import com.cooperationart.cr.config.ZelleGatewayConfig;
import com.cooperationart.cr.dto.ZellePaymentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class ZellePaymentGatewayTest {
    private ZellePaymentGateway gateway;

    @BeforeEach
    public void setUp() {
        ZelleGatewayConfig config = new ZelleGatewayConfig("https://api.zellepay.com/v1", "test-key-123", 5000);
        gateway = new ZellePaymentGateway(config);
    }

    @Test
    public void testSendPayment_HappyPath_SmallAmount() {
        ZellePaymentRequest request = new ZellePaymentRequest("123", "client@example.com", new BigDecimal("100.00"));
        assertDoesNotThrow(() -> gateway.sendPayment(request));
    }

    @Test
    public void testSendPayment_HappyPath_MediumAmount() {
        ZellePaymentRequest request = new ZellePaymentRequest("123", "client@example.com", new BigDecimal("450.00"));
        assertDoesNotThrow(() -> gateway.sendPayment(request));
    }
}
