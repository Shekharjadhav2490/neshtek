package com.neshtek.monitor.analytics;

public record MonitorAnalytics(
        long totalChecks,
        long successfulChecks,
        long failedChecks,
        long gatewayFailures,
        double uptimePercent,
        double averageResponseTimeMs) {
}
