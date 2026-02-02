package org.example.web.utils.web;

import org.example.web.RequestHandler;
import org.example.web.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class RoutePattern {
    private final String pattern;
    private final PathType pathType;
    private final List<Pair<String, String>> segments;
    private RequestHandler requestHandler;

    private RoutePattern(String pattern, PathType pathType, List<Pair<String, String>> segments) {
        this.pattern = pattern;
        this.pathType = pathType;
        this.segments = segments;
        this.requestHandler = null;
    }

    public static RoutePattern parse(String path){
        String[] split = path.split("/");
        ArrayList<Pair<String, String>> segments = new ArrayList<>();
//        ArrayList<String> variableNames = new ArrayList<>();
        boolean variableExists = false;
        for (String s : split) {
            if(s.startsWith("{") && s.endsWith("}")){
                variableExists = true;
                segments.add(new Pair<>("*", s.substring(1,s.length()-1)));
            }else{
                segments.add(new Pair<>(s, s));
            }
        }
        PathType pathType = variableExists ? PathType.DYNAMIC : PathType.STATIC;
        return new RoutePattern(path, pathType, segments);
    }

    public String getPattern() {
        return pattern;
    }

    public PathType getPathType() {
        return pathType;
    }

    public List<Pair<String, String>> getSegments() {
        return segments;
    }


    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public RequestHandler getRequestHandler() {
        return requestHandler;
    }

    @Override
    public String toString() {
        return "RoutePattern{" +
                "pattern='" + pattern + '\'' +
                ", pathType=" + pathType +
                ", segments=" + segments +
                ", requestHandler=" + requestHandler +
                '}';
    }
}
