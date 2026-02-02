package org.example.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public enum Fog {
    FOGGER
    ;
    private final BlockingQueue<LogMessage> logQueue = new LinkedBlockingQueue<>();
    private final Thread loggingThread;
    private volatile boolean running = true;
    Fog() {
        loggingThread = new Thread(this::processLogs, "AsyncLoggerThread");
        loggingThread.setDaemon(true); // Allow JVM to exit if only daemon threads remain
        loggingThread.start();
    }

    public void log(Object message) {
        try {
            logQueue.put(new LogMessage(message.toString()));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error adding log message to queue: " + e.getMessage());
        }
    }
    private void processLogs() {
        while (running || !logQueue.isEmpty()) {
            try {
                LogMessage logMessage = logQueue.take();
                // In a real application, you would write to a file, database, or network
                System.out.println("ASYNC LOG: " + logMessage);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Logging thread interrupted: " + e.getMessage());
            }
        }
    }

    public void shutdown() {
        running = false;
        loggingThread.interrupt(); // Interrupt the thread to unblock from take()
        try {
            loggingThread.join(); // Wait for the thread to finish processing remaining logs
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
