package org.example.web.handlers;

import org.example.web.RequestHandler;

public class HealthCheckHandler implements RequestHandler {
    @Override
    public Object get() {
        return "OK!";
    }

    @Override
    public String getUrl() {
        return "/health";
    }
}
