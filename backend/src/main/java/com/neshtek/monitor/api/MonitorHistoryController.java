package com.neshtek.monitor.api;

import com.neshtek.monitor.check.MonitorCheck;
import com.neshtek.monitor.check.MonitorCheckRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/monitors")
public class MonitorHistoryController {
    private final MonitorCheckRepository repository;
    public MonitorHistoryController(MonitorCheckRepository repository) { this.repository = repository; }
    @GetMapping("/{id}/checks")
    public Page<MonitorCheck> history(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
        int safeSize = Math.min(Math.max(size, 1), 200);
        return repository.findByMonitorIdOrderByCheckedAtDesc(id, PageRequest.of(Math.max(page, 0), safeSize));
    }
}
