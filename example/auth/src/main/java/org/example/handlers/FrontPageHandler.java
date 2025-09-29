package org.example.handlers;

import org.example.web.RequestHandler;
import org.example.web.request.HttpRequest;

public class FrontPageHandler implements RequestHandler<Void> {
    @Override
    public Object doHandle(HttpRequest<Void> httpRequest) {
        return """
                <html lang="en">
                <head>
                    <title>Hello!</title>
                </head>
                <body>
                    <div>hello!</div>
                </body>
                </html>""";
    }

    @Override
    public String getUrl() {
        return "/";
    }
}
