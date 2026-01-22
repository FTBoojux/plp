package org.example;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CompareIOThreadPools {
    static int taskCount = 10_000;
    public static void main(String[] args) {
        System.out.println("IO密集型任务测试:");
        // 虚拟线程池
        try (ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            verify("虚拟", virtualExecutor, new IOIntensiveTask());
        }
        // 传统线程池
        try (ExecutorService traditionalExecutor = Executors.newFixedThreadPool(200)) {
            verify("传统", traditionalExecutor, new IOIntensiveTask());
        }
        // CPU密集型任务测试
        System.out.println("CPU密集型任务测试:");
        // 虚拟线程池
        try (ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            verify("虚拟", virtualExecutor, new CpuIntensiveTask());
        }
        // 传统线程池
        try (ExecutorService traditionalExecutor = Executors.newFixedThreadPool(200)) {
            verify("传统", traditionalExecutor, new CpuIntensiveTask());
        }
    }
    public static void verify(String type, ExecutorService threadPool, Runnable task) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < taskCount; i++) {
            threadPool.submit(task);
        }
        // 等待所有任务完成
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
        }
        long endTime = System.currentTimeMillis();
        long total = endTime - startTime;
        float avgTime = total / (float) taskCount;
        System.out.println(type + "线程池总耗时: " + total + " ms, 平均每个任务耗时: " + avgTime + " ms");
    }
}

class IOIntensiveTask implements Runnable {
    @Override
    public void run() {
        try {
            // 模拟IO密集型任务
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
    }
}

class CpuIntensiveTask implements Runnable {
    @Override
    public void run() {
        // 模拟CPU密集型任务
        double pi = 0;
        for (int i = 0; i < 20_000_000; i++) {
            if (i % 2 == 0) {
                pi += 1.0 / (2 * i + 1);
            } else {
                pi -= 1.0 / (2 * i + 1);
            }
        }
    }
}
