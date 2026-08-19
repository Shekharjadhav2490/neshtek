package com.neshtek.monitor.monitor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MonitorService {

    private final MonitorRepository repository;

    public MonitorService(MonitorRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Monitor> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Monitor findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new MonitorNotFoundException(id));
    }

    @Transactional
    public Monitor create(Monitor monitor) {
        return repository.save(monitor);
    }

    @Transactional
    public Monitor update(Long id, Monitor input) {
        Monitor existing = findById(id);
        existing.setName(input.getName());
        existing.setUrl(input.getUrl());
        existing.setCheckIntervalSeconds(input.getCheckIntervalSeconds());
        existing.setTimeoutSeconds(input.getTimeoutSeconds());
        existing.setExpectedStatus(input.getExpectedStatus());
        existing.setStatus(input.getStatus());
        existing.touch();
        return repository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Monitor existing = findById(id);
        repository.delete(existing);
    }
}
