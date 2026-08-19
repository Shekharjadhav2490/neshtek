package com.neshtek.monitor.alert;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertConfigRepository extends JpaRepository<AlertConfig, Long> {
    List<AlertConfig> findByMonitorIdAndEnabledTrue(Long monitorId);
}
