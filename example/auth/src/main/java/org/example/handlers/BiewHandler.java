package org.example.handlers;

import org.example.web.RequestHandler;
import org.example.web.biew.Biew;
import org.example.web.request.HttpRequest;

public class BiewHandler implements RequestHandler<Void> {
    @Override
    public Object doHandle(HttpRequest<Void> httpRequest) {
        return new Biew("home.html");
    }

    @Override
    public String getUrl() {
        return "/biew";
    }
}
