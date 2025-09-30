package org.example.domain;

public class Response {
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Response(int code) {
        this.code = code;
    }

    public Response() {
    }

    private int code;
}
