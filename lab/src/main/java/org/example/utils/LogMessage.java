package org.example.utils;

public class LogMessage {
    private final String message;
    private final long timestamp;

    public LogMessage(String message) {
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s", timestamp, message);
    }
}
