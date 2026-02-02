package org.example.web.utils.web;

import org.example.web.RequestHandler;

import java.util.HashMap;

public class MatchResult {
    public RequestHandler requestHandler;
    public HashMap<String, String> pathVariables;

    public MatchResult(RequestHandler requestHandler, HashMap<String, String> pathVariables) {
        this.requestHandler = requestHandler;
        this.pathVariables = pathVariables;
    }

    @Override
    public String toString() {
        return "MatchResult{" +
                "requestHandler=" + requestHandler +
                ", pathVariables=" + pathVariables +
                '}';
    }
}
