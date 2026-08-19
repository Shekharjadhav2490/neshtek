package com.neshtek.monitor.api;

import com.neshtek.monitor.analytics.MonitorAnalytics;
import com.neshtek.monitor.analytics.MonitorAnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/monitors")
public class MonitorAnalyticsController {

    private final MonitorAnalyticsService service;

    public MonitorAnalyticsController(MonitorAnalyticsService service) {
        this.service = service;
    }

    @GetMapping("/{id}/analytics")
    public MonitorAnalytics analytics(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1000") int sampleSize) {
        return service.calculate(id, sampleSize);
    }
}
