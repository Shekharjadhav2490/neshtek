package com.neshtek.monitor.alert;

import com.neshtek.monitor.monitor.Monitor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ALERT_CONFIGS")
public class AlertConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MONITOR_ID", nullable = false)
    private Monitor monitor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AlertChannel channel = AlertChannel.EMAIL;

    @Column(name = "RECIPIENT", nullable = false, length = 320)
    private String recipient;

    @Column(name = "ENABLED", nullable = false)
    private boolean enabled = true;

    @Column(name = "ON_FAILURE", nullable = false)
    private boolean onFailure = true;

    @Column(name = "ON_RECOVERY", nullable = false)
    private boolean onRecovery = true;

    public Long getId() { return id; }
    public Monitor getMonitor() { return monitor; }
    public void setMonitor(Monitor monitor) { this.monitor = monitor; }
    public AlertChannel getChannel() { return channel; }
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public boolean isOnFailure() { return onFailure; }
    public boolean isOnRecovery() { return onRecovery; }
}
