package org.example.exceptions;

public class ProtocolViolationException extends Exception{
    public ProtocolViolationException(String format) {
        super(format);
    }
}
