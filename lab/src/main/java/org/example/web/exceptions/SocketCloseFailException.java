package org.example.web.exceptions;

public class SocketCloseFailException extends RuntimeException {
    public SocketCloseFailException(String message) {
        super(message);
    }
}
