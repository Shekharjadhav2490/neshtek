package com.neshtek.monitor.monitor;

public record MonitorCheckResult(
        boolean available,
        boolean expectedStatus,
        int httpStatus,
        long responseTimeMs,
        String errorMessage,
        MonitorOutcome outcome) {

    public boolean isGatewayFailure() {
        return outcome == MonitorOutcome.GATEWAY_FAILURE;
    }

    public boolean isFailure() {
        return outcome != MonitorOutcome.UP;
    }
}
