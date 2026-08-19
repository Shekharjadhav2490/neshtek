package com.neshtek.monitor.check;

import com.neshtek.monitor.monitor.Monitor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "MONITOR_CHECKS")
public class MonitorCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MONITOR_ID", nullable = false)
    private Monitor monitor;

    @Column(name = "HTTP_STATUS")
    private Integer httpStatus;

    @Column(name = "RESPONSE_TIME_MS")
    private Long responseTimeMs;

    @Column(nullable = false, length = 30)
    private String outcome;

    @Column(name = "ERROR_MESSAGE", length = 2000)
    private String errorMessage;

    @Column(name = "CHECKED_AT", nullable = false)
    private Instant checkedAt = Instant.now();

    public Long getId() { return id; }
    public Monitor getMonitor() { return monitor; }
    public void setMonitor(Monitor monitor) { this.monitor = monitor; }
    public Integer getHttpStatus() { return httpStatus; }
    public void setHttpStatus(Integer value) { this.httpStatus = value; }
    public Long getResponseTimeMs() { return responseTimeMs; }
    public void setResponseTimeMs(Long value) { this.responseTimeMs = value; }
    public String getOutcome() { return outcome; }
    public void setOutcome(String value) { this.outcome = value; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String value) { this.errorMessage = value; }
    public Instant getCheckedAt() { return checkedAt; }
}
