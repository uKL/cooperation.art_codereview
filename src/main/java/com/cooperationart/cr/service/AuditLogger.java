package com.cooperationart.cr.service;

import java.math.BigDecimal;

public interface AuditLogger {
    void logTransaction(String transactionId, String source, String target, BigDecimal amount, String type);
}
