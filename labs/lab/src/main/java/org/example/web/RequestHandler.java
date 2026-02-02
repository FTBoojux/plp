package org.example.web;

import org.example.enums.HTTPEnum;
import org.example.web.request.HttpRequest;

public interface RequestHandler<T> {
    Object doHandle(HttpRequest<T> httpRequest);
    String getUrl();
    default String getType(){
        return HTTPEnum.GET.type;
    }
}
