package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Demonstrates: "Virtual threads are not faster threads; they do not run code
 * any faster than platform threads. They exist to provide scale (higher throughput),
 * not speed (lower latency)."
 */
@RestController
public class ExampleController {

    private static final int PLATFORM_THREAD_POOL_SIZE = 10;
    private final ExecutorService platformThreadPool = Executors.newFixedThreadPool(PLATFORM_THREAD_POOL_SIZE);

    /**
     * Platform threads with fixed pool.
     * Throughput is limited by pool size - tasks queue when pool is exhausted.
     */
    @GetMapping("/platform")
    public ThroughputResult platformThread(@RequestParam(defaultValue = "100") int taskCount,
                                           @RequestParam(defaultValue = "100") int ioLatencyMs) {
        return measureThroughput(platformThreadPool, taskCount, ioLatencyMs, "Platform", PLATFORM_THREAD_POOL_SIZE);
    }

    /**
     * Virtual threads - one per task.
     * Throughput scales with task count since virtual threads are cheap.
     */
    @GetMapping("/virtual")
    public ThroughputResult virtualThread(@RequestParam(defaultValue = "100") int taskCount,
                                          @RequestParam(defaultValue = "100") int ioLatencyMs) {
        try (ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            return measureThroughput(virtualExecutor, taskCount, ioLatencyMs, "Virtual", taskCount);
        }
    }

    private ThroughputResult measureThroughput(ExecutorService executor, int taskCount,
                                               int ioLatencyMs, String type, int maxConcurrency) {
        List<Future<TaskMetrics>> futures = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < taskCount; i++) {
            futures.add(executor.submit(() -> simulateBlockingIO(ioLatencyMs)));
        }

        List<TaskMetrics> metrics = collectMetrics(futures);
        long totalDuration = System.currentTimeMillis() - startTime;

        long avgLatency = (long) metrics.stream().mapToLong(TaskMetrics::latencyMs).average().orElse(0);
        double throughput = taskCount * 1000.0 / totalDuration;

        return new ThroughputResult(
                type,
                taskCount,
                ioLatencyMs,
                maxConcurrency,
                avgLatency,
                totalDuration,
                throughput,
                metrics.stream().map(TaskMetrics::threadName).distinct().limit(3).toList()
        );
    }

    private TaskMetrics simulateBlockingIO(int ioLatencyMs) {
        String threadName = Thread.currentThread().toString();
        long start = System.currentTimeMillis();
        try {
            Thread.sleep(ioLatencyMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long latency = System.currentTimeMillis() - start;
        return new TaskMetrics(threadName, latency);
    }

    private List<TaskMetrics> collectMetrics(List<Future<TaskMetrics>> futures) {
        List<TaskMetrics> results = new ArrayList<>();
        for (Future<TaskMetrics> future : futures) {
            try {
                results.add(future.get());
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
        return results;
    }

    record TaskMetrics(String threadName, long latencyMs) {}

    public record ThroughputResult(
            String threadType,
            int taskCount,
            int ioLatencyMs,
            int maxConcurrency,
            long avgLatencyPerTaskMs,
            long totalDurationMs,
            double throughputTasksPerSec,
            List<String> sampleThreadNames
    ) {}
}
