export type MonitorStatus = 'ACTIVE' | 'PAUSED';

export interface Monitor {
  id?: number;
  name: string;
  url: string;
  checkIntervalSeconds: number;
  timeoutSeconds: number;
  expectedStatus: number;
  status: MonitorStatus;
  createdAt?: string;
  updatedAt?: string;
}

export interface MonitorCheckResult {
  available: boolean;
  expectedStatus: boolean;
  httpStatus: number;
  responseTimeMs: number;
  errorMessage?: string | null;
  outcome: 'UP' | 'UNEXPECTED_STATUS' | 'GATEWAY_FAILURE' | 'TIMEOUT' | 'NETWORK_ERROR';
}

export interface MonitorAnalytics {
  totalChecks: number;
  successfulChecks: number;
  failedChecks: number;
  gatewayFailures: number;
  uptimePercent: number;
  averageResponseTimeMs: number;
}
