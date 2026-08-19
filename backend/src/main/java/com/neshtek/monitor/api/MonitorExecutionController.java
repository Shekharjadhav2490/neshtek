package com.neshtek.monitor.api;

import com.neshtek.monitor.monitor.Monitor;
import com.neshtek.monitor.monitor.MonitorCheckResult;
import com.neshtek.monitor.monitor.MonitorExecutionService;
import com.neshtek.monitor.monitor.MonitorService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/monitors")
public class MonitorExecutionController {

    private final MonitorService monitorService;
    private final MonitorExecutionService executionService;

    public MonitorExecutionController(
            MonitorService monitorService,
            MonitorExecutionService executionService) {
        this.monitorService = monitorService;
        this.executionService = executionService;
    }

    @PostMapping("/{id}/check")
    public MonitorCheckResult checkNow(@PathVariable Long id) {
        Monitor monitor = monitorService.findById(id);
        return executionService.checkMonitor(monitor);
    }
}
