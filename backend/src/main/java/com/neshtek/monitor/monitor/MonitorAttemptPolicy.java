package com.neshtek.monitor.monitor;

import org.springframework.stereotype.Component;

@Component
public class MonitorAttemptPolicy {

    private final int attempts;

    public MonitorAttemptPolicy() {
        this.attempts = 2;
    }

    public int attempts() {
        return attempts;
    }
}
