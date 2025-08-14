package org.example.handler;

import org.example.web.RequestHandler;
import org.example.web.request.HttpRequest;

public class TimeOutHandler implements RequestHandler<Void> {
    @Override
    public Object doHandle(HttpRequest<Void> httpRequest) {
        while (!Thread.interrupted()){

        }
        return "";
    }

    @Override
    public String getUrl() {
        return "/timeout";
    }
}
