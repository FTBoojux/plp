package org.example.web.middleware;

import org.example.web.request.HttpRequest;

public interface MiddlewareHandler {
    boolean handle(HttpRequest<?> httpRequest);
}
