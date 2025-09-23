package org.example.web.middleware;

import org.example.web.request.HttpRequest;
import org.example.web.utils.http.HttpResponseBuilder;

public interface MiddlewareHandler {
    boolean handle(HttpRequest<?> httpRequest, HttpResponseBuilder response);
}
