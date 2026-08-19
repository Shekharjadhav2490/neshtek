package com.neshtek.monitor.monitor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MonitorScheduler {

    private final MonitorExecutionService executionService;

    public MonitorScheduler(MonitorExecutionService executionService) {
        this.executionService = executionService;
    }

    @Scheduled(fixedDelayString = "${monitor.scheduler.delay-ms:60000}")
    public void execute() {
        executionService.checkActiveMonitors();
    }
}
