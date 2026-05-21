package com.cooperationart.cr.config;

public class ZelleGatewayConfig {
    private final String apiUrl;
    private final String apiKey;
    private final int timeoutMs;

    public ZelleGatewayConfig(String apiUrl, String apiKey, int timeoutMs) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.timeoutMs = timeoutMs;
    }

    public String getApiUrl() { return apiUrl; }
    public String getApiKey() { return apiKey; }
    public int getTimeoutMs() { return timeoutMs; }
}
