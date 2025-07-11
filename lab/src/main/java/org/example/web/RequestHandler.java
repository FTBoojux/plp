package org.example.web;

import org.example.web.request.HttpRequest;

public interface RequestHandler<T> {
    default Object get(HttpRequest<T> httpRequest){
        String msg = String.join("", "The implement: ", getUrl(), "have not be implemented yet!");
        throw new RuntimeException(msg);
    }
    String getUrl();
}
