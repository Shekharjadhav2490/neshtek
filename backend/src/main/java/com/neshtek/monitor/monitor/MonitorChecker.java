package com.neshtek.monitor.monitor;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

@Component
public class MonitorChecker {

    public MonitorCheckResult check(Monitor monitor) {
        long started = System.nanoTime();

        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(monitor.getTimeoutSeconds()))
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(monitor.getUrl()))
                    .timeout(Duration.ofSeconds(monitor.getTimeoutSeconds()))
                    .header("User-Agent", "Neshtek-Monitor/0.1")
                    .GET()
                    .build();

            HttpResponse<Void> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.discarding());

            long elapsed = elapsedMs(started);
            int status = response.statusCode();

            if (status == monitor.getExpectedStatus()) {
                return new MonitorCheckResult(true, true, status, elapsed, null, MonitorOutcome.UP);
            }

            MonitorOutcome outcome = status == 502
                    ? MonitorOutcome.GATEWAY_FAILURE
                    : MonitorOutcome.UNEXPECTED_STATUS;

            return new MonitorCheckResult(
                    false,
                    false,
                    status,
                    elapsed,
                    "Unexpected HTTP status: " + status,
                    outcome);
        } catch (HttpTimeoutException ex) {
            return failure(started, MonitorOutcome.TIMEOUT, "Request timed out");
        } catch (Exception ex) {
            return failure(started, MonitorOutcome.NETWORK_ERROR, ex.getMessage());
        }
    }

    private MonitorCheckResult failure(long started, MonitorOutcome outcome, String message) {
        return new MonitorCheckResult(false, false, 0, elapsedMs(started), message, outcome);
    }

    private long elapsedMs(long started) {
        return Duration.ofNanos(System.nanoTime() - started).toMillis();
    }
}
