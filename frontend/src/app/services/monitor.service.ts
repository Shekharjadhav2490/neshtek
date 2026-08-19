import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Monitor, MonitorAnalytics, MonitorCheckResult } from '../models/monitor.model';
import { MonitorHistoryPage } from '../models/monitor-history.model';

@Injectable({ providedIn: 'root' })
export class MonitorService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/v1/monitors';
  getAll(): Observable<Monitor[]> { return this.http.get<Monitor[]>(this.apiUrl); }
  create(monitor: Monitor): Observable<Monitor> { return this.http.post<Monitor>(this.apiUrl, monitor); }
  update(id: number, monitor: Monitor): Observable<Monitor> { return this.http.put<Monitor>(`${this.apiUrl}/${id}`, monitor); }
  delete(id: number): Observable<void> { return this.http.delete<void>(`${this.apiUrl}/${id}`); }
  checkNow(id: number): Observable<MonitorCheckResult> { return this.http.post<MonitorCheckResult>(`${this.apiUrl}/${id}/check`, {}); }
  analytics(id: number): Observable<MonitorAnalytics> { return this.http.get<MonitorAnalytics>(`${this.apiUrl}/${id}/analytics`); }
  history(id: number, page = 0, size = 25): Observable<MonitorHistoryPage> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<MonitorHistoryPage>(`${this.apiUrl}/${id}/checks`, { params });
  }
}
