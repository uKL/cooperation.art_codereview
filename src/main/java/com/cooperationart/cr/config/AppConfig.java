package com.cooperationart.cr.config;

public class AppConfig {
    private final String environment;
    private final boolean auditEnabled;

    public AppConfig(String environment, boolean auditEnabled) {
        this.environment = environment;
        this.auditEnabled = auditEnabled;
    }

    public String getEnvironment() { return environment; }
    public boolean isAuditEnabled() { return auditEnabled; }
}
