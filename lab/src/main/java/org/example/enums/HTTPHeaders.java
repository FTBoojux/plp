package org.example.enums;

public enum HTTPHeaders {
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length");
    private final String header;

    HTTPHeaders(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }
}
