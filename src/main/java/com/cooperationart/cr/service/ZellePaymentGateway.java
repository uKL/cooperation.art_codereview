package com.cooperationart.cr.service;

import com.cooperationart.cr.config.ZelleGatewayConfig;
import com.cooperationart.cr.dto.ZellePaymentRequest;
import com.cooperationart.cr.exception.ZelleGatewayException;

public class ZellePaymentGateway {
    private final ZelleGatewayConfig config;

    public ZellePaymentGateway(ZelleGatewayConfig config) {
        this.config = config;
    }

    /**
     * Sends payment to the external Zelle API.
     * 
     * @param request the payment request to send
     * @throws ZelleGatewayException if the payment request is invalid or transmission fails
     */
    public void sendPayment(ZellePaymentRequest request) {
        if (request == null) {
            throw new ZelleGatewayException("Request cannot be null");
        }
        
        // Simulating external network connection and payload transmission
        System.out.println("Connecting to Zelle endpoint: " + config.getApiUrl());
        System.out.println("Sending Zelle payment of " + request.getAmount() + " to " + request.getRecipientEmail());
        
        // Simulates successful API response from Zelle sandbox
    }
}
