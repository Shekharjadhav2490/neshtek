package com.neshtek.monitor.analytics;

import com.neshtek.monitor.check.MonitorCheck;
import com.neshtek.monitor.check.MonitorCheckRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitorAnalyticsService {

    private final MonitorCheckRepository repository;

    public MonitorAnalyticsService(MonitorCheckRepository repository) {
        this.repository = repository;
    }

    public MonitorAnalytics calculate(Long monitorId, int sampleSize) {
        int safeSize = Math.min(Math.max(sampleSize, 1), 10000);
        List<MonitorCheck> checks = repository
                .findByMonitorIdOrderByCheckedAtDesc(monitorId, PageRequest.of(0, safeSize))
                .getContent();

        if (checks.isEmpty()) {
            return new MonitorAnalytics(0, 0, 0, 0, 0, 0);
        }

        long success = checks.stream().filter(c -> "UP".equals(c.getOutcome())).count();
        long gateway = checks.stream().filter(c -> "GATEWAY_FAILURE".equals(c.getOutcome())).count();
        long failed = checks.size() - success;
        double uptime = (success * 100.0) / checks.size();
        double average = checks.stream()
                .filter(c -> c.getResponseTimeMs() != null)
                .mapToLong(MonitorCheck::getResponseTimeMs)
                .average()
                .orElse(0);

        return new MonitorAnalytics(checks.size(), success, failed, gateway, uptime, average);
    }
}
