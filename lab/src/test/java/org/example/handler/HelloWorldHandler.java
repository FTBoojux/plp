package org.example.handler;

import org.example.web.RequestHandler;
import org.example.web.request.HttpRequest;

public class HelloWorldHandler implements RequestHandler<Void> {
    @Override
    public Object doHandle(HttpRequest<Void> httpRequest) {
        return "<html>" +
                "<body>" +
                "<p>hello</p>" +
                "</body>" +
                "</html>";
    }

    @Override
    public String getUrl() {
        return "/hello";
    }
}
