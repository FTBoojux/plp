package org.example.handlers;

import org.example.web.RequestHandler;
import org.example.web.biew.Biew;
import org.example.web.request.HttpRequest;

import java.util.UUID;

public class CookieHandler implements RequestHandler<Void> {
    @Override
    public Object doHandle(HttpRequest<Void> httpRequest) {
        httpRequest.getCookie().add("cookie-id", UUID.randomUUID().toString());
        return new Biew("cookie.html");
    }

    @Override
    public String getUrl() {
        return "/cookie";
    }
}
