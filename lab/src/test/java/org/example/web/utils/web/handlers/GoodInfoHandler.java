package org.example.web.utils.web.handlers;

import org.example.web.RequestHandler;
import org.example.web.request.HttpRequest;

import java.util.HashMap;

public class GoodInfoHandler implements RequestHandler<HashMap<String,String>> {
    @Override
    public Object get(HttpRequest<HashMap<String, String>> httpRequest) {
        String goodId = httpRequest.getPathVariables().getOrDefault("goodId", "");
        return "goodId:" + goodId;
    }

    @Override
    public String getUrl() {
        return "/{goodId}/goodInfo";
    }
}
