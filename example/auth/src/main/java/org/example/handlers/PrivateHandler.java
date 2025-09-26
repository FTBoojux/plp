package org.example.handlers;

import org.example.web.RequestHandler;
import org.example.web.request.HttpRequest;

public class PrivateHandler implements RequestHandler<Void> {
    @Override
    public Object doHandle(HttpRequest<Void> httpRequest) {
        return """
                <html lang="en">
                <head>
                    <title>Private!</title>
                </head>
                <body>
                    <div>Private</div>
                </body>
                </html>""";
    }

    @Override
    public String getUrl() {
        return "/auth/private";
    }
}
