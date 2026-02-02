package org.example.web.exceptions;

public class DuplicatedUrlException extends RuntimeException {
    public DuplicatedUrlException(String url) {
        super("url: " + url + " is duplicated!");
    }
}
