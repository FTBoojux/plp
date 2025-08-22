package org.example.enums;

public enum HTTPHeadersEnum {
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    BOUNDARY("boundary")
    ;
    private final String header;

    HTTPHeadersEnum(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }
}
