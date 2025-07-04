package org.example.web.exceptions;

public class PortUsedException extends RuntimeException {
    public PortUsedException(int port) {
        super(port + " can not be used now!");
    }
}
