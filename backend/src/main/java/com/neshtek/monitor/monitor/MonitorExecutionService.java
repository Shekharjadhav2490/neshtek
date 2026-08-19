package com.neshtek.monitor.monitor;

import com.neshtek.monitor.check.MonitorCheck;
import com.neshtek.monitor.check.MonitorCheckRepository;
import com.neshtek.monitor.incident.Incident;
import com.neshtek.monitor.incident.IncidentRepository;
import com.neshtek.monitor.incident.IncidentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class MonitorExecutionService {

    private final MonitorRepository monitorRepository;
    private final IncidentRepository incidentRepository;
    private final MonitorCheckRepository checkRepository;
    private final MonitorAttemptService attemptService;

    public MonitorExecutionService(
            MonitorRepository monitorRepository,
            IncidentRepository incidentRepository,
            MonitorCheckRepository checkRepository,
            MonitorAttemptService attemptService) {
        this.monitorRepository = monitorRepository;
        this.incidentRepository = incidentRepository;
        this.checkRepository = checkRepository;
        this.attemptService = attemptService;
    }

    @Transactional
    public void checkActiveMonitors() {
        List<Monitor> monitors = monitorRepository.findByStatus(MonitorStatus.ACTIVE);
        monitors.forEach(this::checkMonitor);
    }

    @Transactional
    public MonitorCheckResult checkMonitor(Monitor monitor) {
        MonitorCheckResult result = attemptService.checkWithRetry(monitor);
        persistCheck(monitor, result);
        updateIncidentState(monitor, result);
        return result;
    }

    private void persistCheck(Monitor monitor, MonitorCheckResult result) {
        MonitorCheck check = new MonitorCheck();
        check.setMonitor(monitor);
        check.setHttpStatus(result.httpStatus() == 0 ? null : result.httpStatus());
        check.setResponseTimeMs(result.responseTimeMs());
        check.setOutcome(result.outcome().name());
        check.setErrorMessage(result.errorMessage());
        checkRepository.save(check);
    }

    private void updateIncidentState(Monitor monitor, MonitorCheckResult result) {
        List<Incident> incidents = incidentRepository
                .findByMonitorIdOrderByStartedAtDesc(monitor.getId());

        Incident openIncident = incidents.stream()
                .filter(i -> i.getStatus() == IncidentStatus.OPEN)
                .findFirst()
                .orElse(null);

        if (!result.isFailure()) {
            if (openIncident != null) {
                openIncident.setStatus(IncidentStatus.RESOLVED);
                openIncident.setResolvedAt(Instant.now());
                incidentRepository.save(openIncident);
            }
            return;
        }

        if (openIncident == null) {
            Incident incident = new Incident();
            incident.setMonitor(monitor);
            incident.setHttpStatus(result.httpStatus() == 0 ? null : result.httpStatus());
            incident.setResponseTimeMs(result.responseTimeMs());
            incident.setErrorMessage(result.errorMessage());
            incidentRepository.save(incident);
        } else {
            openIncident.setHttpStatus(result.httpStatus() == 0 ? null : result.httpStatus());
            openIncident.setResponseTimeMs(result.responseTimeMs());
            openIncident.setErrorMessage(result.errorMessage());
            incidentRepository.save(openIncident);
        }
    }
}
