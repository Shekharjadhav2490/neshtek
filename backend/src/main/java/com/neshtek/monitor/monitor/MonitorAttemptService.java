package com.neshtek.monitor.monitor;

import org.springframework.stereotype.Service;

@Service
public class MonitorAttemptService {

    private final MonitorChecker checker;
    private final MonitorAttemptPolicy policy;

    public MonitorAttemptService(MonitorChecker checker, MonitorAttemptPolicy policy) {
        this.checker = checker;
        this.policy = policy;
    }

    public MonitorCheckResult checkWithRetry(Monitor monitor) {
        MonitorCheckResult result = checker.check(monitor);
        if (!result.isFailure()) {
            return result;
        }

        MonitorCheckResult last = result;
        for (int attempt = 1; attempt < policy.attempts(); attempt++) {
            last = checker.check(monitor);
            if (!last.isFailure()) {
                return last;
            }
        }
        return last;
    }
}
