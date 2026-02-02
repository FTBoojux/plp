package org.example.enums;

public enum HTTPEnum {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE"),
    CONNECT("CONNECT")
    ;

    public final String type;

    HTTPEnum(String type) {
        this.type = type;
    }
}
