package com.neshtek.monitor.monitor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonitorRepository extends JpaRepository<Monitor, Long> {
    List<Monitor> findByStatus(MonitorStatus status);
}
