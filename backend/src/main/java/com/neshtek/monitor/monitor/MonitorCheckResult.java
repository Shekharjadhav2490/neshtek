package com.neshtek.monitor.monitor;

public record MonitorCheckResult(
        boolean available,
        boolean expectedStatus,
        int httpStatus,
        long responseTimeMs,
        String errorMessage) {

    public boolean isGatewayFailure() {
        return httpStatus == 502;
    }
}
