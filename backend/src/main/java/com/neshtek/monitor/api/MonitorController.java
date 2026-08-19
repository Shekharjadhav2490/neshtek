package com.neshtek.monitor.api;

import com.neshtek.monitor.monitor.Monitor;
import com.neshtek.monitor.monitor.MonitorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/monitors")
public class MonitorController {

    private final MonitorService service;

    public MonitorController(MonitorService service) {
        this.service = service;
    }

    @GetMapping
    public List<Monitor> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Monitor findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<Monitor> create(@Valid @RequestBody Monitor monitor) {
        Monitor created = service.create(monitor);
        return ResponseEntity.created(URI.create("/api/v1/monitors/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public Monitor update(@PathVariable Long id, @Valid @RequestBody Monitor monitor) {
        return service.update(id, monitor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
