package framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SimpleStressTest {
    private final String targetUrl;
    private final int totalRequest;
    private final int currentThreads;
    private final List<Long> responseTimes = Collections.synchronizedList(new ArrayList<>());

    public SimpleStressTest(String targetUrl, int requests, int threads) {
        this.targetUrl = targetUrl;
        this.totalRequest = requests;
        this.currentThreads = threads;
    }
    public void runTest() throws InterruptedException {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(this.totalRequest);
        for (int i = 0; i < totalRequest; i++) {
            new Thread(()->{
                try {
                    startSignal.await();
                    long responseTime = measureSingleRequest();
                    if(responseTime > 0){
                        responseTimes.add(responseTime);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(e.getMessage());
                } finally {
                    doneSignal.countDown();
                }
            }).start();
        }
        long testStartTime = System.currentTimeMillis();
        startSignal.countDown();

        doneSignal.await();
        long testFinishTime = System.currentTimeMillis();

        analyzeResults(testFinishTime - testStartTime);
    }

    private void analyzeResults(long runTime) {
        if(this.responseTimes.isEmpty()){
            System.out.println("no successful response");
            return;
        }
        Collections.sort(this.responseTimes);
        long sum = responseTimes.stream().mapToLong(Long::longValue).sum();
        double averageTime = sum / (double)this.responseTimes.size();

        System.out.println("=== Stress Test Results ===");
        System.out.println("Total requests: " + this.totalRequest);
        System.out.println("Successful requests: " + responseTimes.size());
        System.out.println("Total test time: " + runTime + "ms");
        System.out.println("Requests per second: " +
                (responseTimes.size() * 1000.0 / runTime));
        System.out.println("Average response time: " + averageTime + "ms");
        System.out.println("Min response time: " + responseTimes.getFirst() + "ms");
        System.out.println("Max response time: " +
                responseTimes.getLast() + "ms");

        // Percentiles tell you more than averages
        System.out.println("50th percentile: " +
                responseTimes.get(responseTimes.size() / 2) + "ms");
        System.out.println("90th percentile: " +
                responseTimes.get((int)(responseTimes.size() * 0.9)) + "ms");
    }

    private long measureSingleRequest(){
        long startTime = System.currentTimeMillis();
        try {
            URL url = URI.create(this.targetUrl).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                while (reader.readLine() != null) {

                }
            }

            long endTime = System.currentTimeMillis();
            return endTime - startTime;
        } catch (IOException e) {
            System.out.println("request failed: " + e.getMessage());
            return -1;
        }

    }
}
