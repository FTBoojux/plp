package org.example.web.handlers;

import org.example.web.RequestHandler;
import org.example.web.request.HttpRequest;

import java.util.HashMap;

public class HealthCheckHandler implements RequestHandler<HashMap<String,String>> {

    @Override
    public Object get(HttpRequest<HashMap<String, String>> request) {
        return "OK!";
    }

    @Override
    public String getUrl() {
        return "/health";
    }
}
