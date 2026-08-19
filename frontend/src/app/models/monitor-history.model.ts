export interface MonitorCheckHistory {
  id: number;
  httpStatus?: number | null;
  responseTimeMs?: number | null;
  outcome: string;
  errorMessage?: string | null;
  checkedAt: string;
}

export interface MonitorHistoryPage {
  content: MonitorCheckHistory[];
  totalElements: number;
  totalPages: number;
  number: number;
}
