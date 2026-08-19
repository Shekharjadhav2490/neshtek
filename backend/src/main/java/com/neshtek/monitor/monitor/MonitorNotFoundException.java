package com.neshtek.monitor.monitor;

public class MonitorNotFoundException extends RuntimeException {
    public MonitorNotFoundException(Long id) {
        super("Monitor not found: " + id);
    }
}
