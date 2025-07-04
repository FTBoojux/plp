package org.example.web.handlers;

import org.example.web.RequestHandler;

public class HealthCheckHandler implements RequestHandler {
    @Override
    public Object get() {
        return RequestHandler.super.get();
    }

    @Override
    public String getUrl() {
        return "";
    }
}
