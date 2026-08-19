import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Monitor, MonitorAnalytics, MonitorCheckResult } from '../models/monitor.model';
import { MonitorService } from '../services/monitor.service';

@Component({ selector: 'neshtek-dashboard', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './dashboard.component.html' })
export class DashboardComponent implements OnInit {
  private readonly monitorService = inject(MonitorService);
  monitors: Monitor[] = [];
  analytics = new Map<number, MonitorAnalytics>();
  checkResults = new Map<number, MonitorCheckResult>();
  loading = true; error = ''; showForm = false; editingId?: number; saving = false; checkingId?: number;
  selectedMonitor?: Monitor; selectedAnalytics?: MonitorAnalytics;
  form: Monitor = this.emptyMonitor();

  ngOnInit(): void { this.loadMonitors(); }

  loadMonitors(): void {
    this.loading = true; this.error = '';
    this.monitorService.getAll().subscribe({
      next: monitors => { this.monitors = monitors; this.loading = false; monitors.forEach(m => this.loadAnalytics(m)); },
      error: () => { this.loading = false; this.error = 'Unable to connect to the Neshtek Monitor API.'; }
    });
  }

  loadAnalytics(monitor: Monitor): void {
    if (!monitor.id) return;
    this.monitorService.analytics(monitor.id).subscribe({ next: value => { this.analytics.set(monitor.id!, value); if (this.selectedMonitor?.id === monitor.id) this.selectedAnalytics = value; } });
  }

  openCreate(): void { this.editingId = undefined; this.form = this.emptyMonitor(); this.showForm = true; }
  openEdit(monitor: Monitor): void { this.editingId = monitor.id; this.form = { ...monitor }; this.showForm = true; }
  closeForm(): void { this.showForm = false; this.saving = false; }

  saveMonitor(): void {
    this.saving = true;
    const request = this.editingId ? this.monitorService.update(this.editingId, this.form) : this.monitorService.create(this.form);
    request.subscribe({ next: () => { this.closeForm(); this.loadMonitors(); }, error: () => { this.saving = false; this.error = 'Unable to save the monitor.'; } });
  }

  deleteMonitor(monitor: Monitor): void {
    if (!monitor.id || !confirm(`Delete ${monitor.name}?`)) return;
    this.monitorService.delete(monitor.id).subscribe({ next: () => this.loadMonitors(), error: () => this.error = 'Unable to delete the monitor.' });
  }

  checkNow(monitor: Monitor): void {
    if (!monitor.id) return;
    this.checkingId = monitor.id; this.error = '';
    this.monitorService.checkNow(monitor.id).subscribe({
      next: result => { this.checkResults.set(monitor.id!, result); this.checkingId = undefined; this.selectMonitor(monitor); this.loadAnalytics(monitor); },
      error: () => { this.checkingId = undefined; this.error = 'The monitor check failed to reach the API.'; }
    });
  }

  selectMonitor(monitor: Monitor): void { this.selectedMonitor = monitor; this.selectedAnalytics = monitor.id ? this.analytics.get(monitor.id) : undefined; }

  status(monitor: Monitor): 'UP' | 'DOWN' | 'UNKNOWN' {
    const result = monitor.id ? this.checkResults.get(monitor.id) : undefined;
    if (result) return result.outcome === 'UP' ? 'UP' : 'DOWN';
    return 'UNKNOWN';
  }

  statusLabel(monitor: Monitor): string {
    const result = monitor.id ? this.checkResults.get(monitor.id) : undefined;
    if (result?.outcome === 'GATEWAY_FAILURE') return '502 GATEWAY';
    if (result?.outcome === 'TIMEOUT') return 'TIMEOUT';
    return this.status(monitor);
  }

  uptime(monitor: Monitor): string { const value = monitor.id ? this.analytics.get(monitor.id)?.uptimePercent : undefined; return value === undefined ? '--' : `${value.toFixed(2)}%`; }
  response(monitor: Monitor): string { const value = monitor.id ? this.analytics.get(monitor.id)?.averageResponseTimeMs : undefined; return value === undefined ? '--' : `${Math.round(value)} ms`; }
  get totalGatewayFailures(): number { return [...this.analytics.values()].reduce((sum, a) => sum + a.gatewayFailures, 0); }
  get averageResponse(): string { const values = [...this.analytics.values()].map(a => a.averageResponseTimeMs).filter(v => Number.isFinite(v)); return values.length ? Math.round(values.reduce((a, b) => a + b, 0) / values.length).toString() : '--'; }

  private emptyMonitor(): Monitor { return { name: '', url: '', checkIntervalSeconds: 300, timeoutSeconds: 15, expectedStatus: 200, status: 'ACTIVE' }; }
}
