package com.neshtek.monitor.alert;

import com.neshtek.monitor.monitor.Monitor;
import com.neshtek.monitor.monitor.MonitorCheckResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    private static final Logger log = LoggerFactory.getLogger(AlertService.class);

    private final AlertConfigRepository repository;

    public AlertService(AlertConfigRepository repository) {
        this.repository = repository;
    }

    public void notifyFailure(Monitor monitor, MonitorCheckResult result) {
        send(monitor, result, false);
    }

    public void notifyRecovery(Monitor monitor, MonitorCheckResult result) {
        send(monitor, result, true);
    }

    private void send(Monitor monitor, MonitorCheckResult result, boolean recovery) {
        List<AlertConfig> configs = repository.findByMonitorIdAndEnabledTrue(monitor.getId());
        configs.stream()
                .filter(c -> c.getChannel() == AlertChannel.EMAIL)
                .filter(c -> recovery ? c.isOnRecovery() : c.isOnFailure())
                .forEach(c -> log.info(
                        "Neshtek alert recipient={} monitor={} recovery={} outcome={} status={} responseMs={}",
                        c.getRecipient(), monitor.getName(), recovery, result.outcome(),
                        result.httpStatus(), result.responseTimeMs()));
    }
}
