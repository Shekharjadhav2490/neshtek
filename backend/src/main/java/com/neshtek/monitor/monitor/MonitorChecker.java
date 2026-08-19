package com.neshtek.monitor.monitor;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

            long elapsed = Duration.ofNanos(System.nanoTime() - started).toMillis();
            int status = response.statusCode();
            boolean expected = status == monitor.getExpectedStatus();

            return new MonitorCheckResult(
                    expected,
                    expected,
                    status,
                    elapsed,
                    expected ? null : "Unexpected HTTP status: " + status);
        } catch (Exception ex) {
            long elapsed = Duration.ofNanos(System.nanoTime() - started).toMillis();
            return new MonitorCheckResult(false, false, 0, elapsed, ex.getMessage());
        }
    }
}
