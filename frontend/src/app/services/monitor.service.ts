import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Monitor, MonitorAnalytics, MonitorCheckResult } from '../models/monitor.model';

@Injectable({ providedIn: 'root' })
export class MonitorService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/v1/monitors';

  getAll(): Observable<Monitor[]> {
    return this.http.get<Monitor[]>(this.apiUrl);
  }

  create(monitor: Monitor): Observable<Monitor> {
    return this.http.post<Monitor>(this.apiUrl, monitor);
  }

  update(id: number, monitor: Monitor): Observable<Monitor> {
    return this.http.put<Monitor>(`${this.apiUrl}/${id}`, monitor);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  checkNow(id: number): Observable<MonitorCheckResult> {
    return this.http.post<MonitorCheckResult>(`${this.apiUrl}/${id}/check`, {});
  }

  analytics(id: number): Observable<MonitorAnalytics> {
    return this.http.get<MonitorAnalytics>(`${this.apiUrl}/${id}/analytics`);
  }
}
