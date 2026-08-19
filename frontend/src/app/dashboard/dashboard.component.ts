import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Monitor, MonitorAnalytics, MonitorCheckResult } from '../models/monitor.model';
import { MonitorService } from '../services/monitor.service';

@Component({
  selector: 'neshtek-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  private readonly monitorService = inject(MonitorService);

  monitors: Monitor[] = [];
  analytics = new Map<number, MonitorAnalytics>();
  checkResults = new Map<number, MonitorCheckResult>();
  loading = true;
  error = '';
  showForm = false;
  editingId?: number;
  saving = false;
  checkingId?: number;

  form: Monitor = this.emptyMonitor();

  ngOnInit(): void {
    this.loadMonitors();
  }

  loadMonitors(): void {
    this.loading = true;
    this.monitorService.getAll().subscribe({
      next: monitors => {
        this.monitors = monitors;
        this.loading = false;
        monitors.forEach(m => this.loadAnalytics(m));
      },
      error: () => {
        this.loading = false;
        this.error = 'Unable to connect to the Neshtek Monitor API.';
      }
    });
  }

  loadAnalytics(monitor: Monitor): void {
    if (!monitor.id) return;
    this.monitorService.analytics(monitor.id).subscribe({
      next: value => this.analytics.set(monitor.id!, value)
    });
  }

  openCreate(): void {
    this.editingId = undefined;
    this.form = this.emptyMonitor();
    this.showForm = true;
  }

  openEdit(monitor: Monitor): void {
    this.editingId = monitor.id;
    this.form = { ...monitor };
    this.showForm = true;
  }

  closeForm(): void {
    this.showForm = false;
    this.saving = false;
  }

  saveMonitor(): void {
    this.saving = true;
    const request = this.editingId
      ? this.monitorService.update(this.editingId, this.form)
      : this.monitorService.create(this.form);

    request.subscribe({
      next: () => {
        this.closeForm();
        this.loadMonitors();
      },
      error: () => {
        this.saving = false;
        this.error = 'Unable to save the monitor. Check the URL and API configuration.';
      }
    });
  }

  deleteMonitor(monitor: Monitor): void {
    if (!monitor.id || !confirm(`Delete ${monitor.name}?`)) return;
    this.monitorService.delete(monitor.id).subscribe({
      next: () => this.loadMonitors(),
      error: () => this.error = 'Unable to delete the monitor.'
    });
  }

  checkNow(monitor: Monitor): void {
    if (!monitor.id) return;
    this.checkingId = monitor.id;
    this.monitorService.checkNow(monitor.id).subscribe({
      next: result => {
        this.checkResults.set(monitor.id!, result);
        this.checkingId = undefined;
        this.loadAnalytics(monitor);
      },
      error: () => {
        this.checkingId = undefined;
        this.error = 'The monitor check failed to reach the API.';
      }
    });
  }

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

  private emptyMonitor(): Monitor {
    return {
      name: '',
      url: '',
      checkIntervalSeconds: 300,
      timeoutSeconds: 15,
      expectedStatus: 200,
      status: 'ACTIVE'
    };
  }
}
