package org.example.web.utils.web.handlers;

import org.example.web.RequestHandler;
import org.example.web.request.HttpRequest;

import java.util.HashMap;

public class UserSpaceHandler implements RequestHandler<HashMap<String,String>> {
    @Override
    public Object doHandle(HttpRequest<HashMap<String, String>> httpRequest) {
        String username = httpRequest.getPathVariables().getOrDefault("username", "");
        return "username=" + username;
    }

    @Override
    public String getUrl() {
        return "/{username}/space";
    }
}
