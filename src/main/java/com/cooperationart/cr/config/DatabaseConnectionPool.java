package com.cooperationart.cr.config;

public class DatabaseConnectionPool {
    private final int maxPoolSize;
    private final int connectionTimeoutMs;

    public DatabaseConnectionPool(int maxPoolSize, int connectionTimeoutMs) {
        this.maxPoolSize = maxPoolSize;
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public int getMaxPoolSize() { return maxPoolSize; }
    public int getConnectionTimeoutMs() { return connectionTimeoutMs; }
}
