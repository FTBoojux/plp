package org.example.web.handlers;

import org.example.web.RequestHandler;
import org.example.web.request.HttpRequest;

import java.util.HashMap;

public class PathVariableHandler implements RequestHandler<HashMap<String,String>> {
    @Override
    public Object get(HttpRequest<HashMap<String, String>> httpRequest) {
        HashMap<String, String> pathVariables = httpRequest.getPathVariables();
        StringBuilder sb = new StringBuilder();
        pathVariables.forEach((k,v)->{
            sb.append(k).append("=").append(v).append("\n");
        });
        return sb.toString();
    }

    @Override
    public String getUrl() {
        return "/{pathVariable}/path";
    }
}
