package org.example.web;

import org.example.web.request.HttpRequest;

public interface RequestHandler<T> {
    Object get(HttpRequest<T> httpRequest);
    String getUrl();
}
