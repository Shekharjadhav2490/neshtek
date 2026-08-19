package com.neshtek.monitor.check;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitorCheckRepository extends JpaRepository<MonitorCheck, Long> {
    Page<MonitorCheck> findByMonitorIdOrderByCheckedAtDesc(Long monitorId, Pageable pageable);
}
