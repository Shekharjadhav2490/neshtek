import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

interface MonitorCard {
  name: string;
  url: string;
  status: 'UP' | 'DOWN' | 'UNKNOWN';
  uptime: string;
  responseTime: string;
  incident: string;
}

@Component({
  selector: 'neshtek-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent {
  monitors: MonitorCard[] = [
    {
      name: 'Example Website',
      url: 'https://example.com',
      status: 'UNKNOWN',
      uptime: '--',
      responseTime: '--',
      incident: 'Not connected'
    }
  ];
}
