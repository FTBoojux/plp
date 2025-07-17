package org.example.web.utils.web;

import org.example.web.RequestHandler;
import org.example.web.utils.Pair;

import java.util.*;

class MatchCandidate {
    private RouteTree node;
    private HashMap<String, String> pathVariables;

    public MatchCandidate(RouteTree node, Map<String, String> pathVariables) {
        this.node = node;
        this.pathVariables = new HashMap<>(pathVariables);
    }

    public RouteTree getNode() {
        return node;
    }

    public void setNode(RouteTree node) {
        this.node = node;
    }

    public HashMap<String, String> getPathVariables() {
        return pathVariables;
    }

    public void setPathVariables(HashMap<String, String> pathVariables) {
        this.pathVariables = pathVariables;
    }
}

public class RouteTree {
    private final String segment;
    private final String path;
    private final Map<String, RouteTree> routes;


    private RequestHandler requestHandler;
    public RouteTree(String segment){
        this.segment = segment;
        this.routes = new HashMap<>();
        this.path = null;
        this.requestHandler = null;
    }
    private RouteTree(String segment, String path){
        this.segment = segment;
        this.path = path;
        this.routes = new HashMap<>();
        this.requestHandler = null;
    }
    public static RouteTree parse(String path){
        return  new RouteTree("*",path);
    }



    public String getSegment() {
        return segment;
    }

    public Map<String, RouteTree> getRoutes() {
        return routes;
    }

    public RequestHandler getRequestHandler() {
        return requestHandler;
    }
    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }
    /**
     * add a route node to current route node
     */
    public void addRoute(String segment, RouteTree route){
        routes.put(segment, route);
    }

    /**
     * use route-pattern's segment-route, and use current route node as root
     * @param route
     */
    public void addRoute(RoutePattern route){
        RouteTree node = this;
        List<Pair<String, String>> segments = route.getSegments();
        for (int i = 0; i < segments.size(); i++) {
            String segment = segments.get(i).first;
            if(!node.routes.containsKey(segment)){
                node.routes.put(segment, new RouteTree(segment,segments.get(i).second));
            }
            if(i+1 == segments.size()){
                node.routes.get(segment).setRequestHandler(route.getRequestHandler());
                return;
            }
            node = node.routes.get(segment);
        }
    }
    public RouteTree find(String path){
        String[] split = path.split("/");
        List<String> segments = Arrays.asList(split);
        Queue<RouteTree> queue = new ArrayDeque<>();
        queue.add(this);
        int idx = 0,length = segments.size();
        while(idx < length && !queue.isEmpty()){
            int size = queue.size();
            for(int i = size; i > 0; --i){
                RouteTree current = queue.poll();
                if( null == current.getRoutes() || current.getRoutes().isEmpty() ){
                    continue;
                }
                RouteTree routeTree = current.getRoutes().get(split[idx]);
                if(routeTree != null){
                    queue.add(routeTree);
                }
                RouteTree routeTree1 = current.getRoutes().get("*");
                if(routeTree1 != null){
                    queue.add(routeTree1);
                }
            }
            ++idx;
            if(idx == length && !queue.isEmpty()){
                for(int i = queue.size(); i > 0; --i){
                    RouteTree current = queue.poll();
                    if(current.getRequestHandler() != null){
                        return current;
                    }else{
                        queue.add(current);
                    }
                }
            }
        }
        return null;
    }

    public MatchResult findWithVariable(String path){
        String[] split = path.split("/");
        Queue<MatchCandidate> queue = new ArrayDeque<>();
        queue.add(new MatchCandidate(this, new HashMap<>()));
        for (String segment : split) {
            Queue<MatchCandidate> nextLevel = new ArrayDeque<>();
            while (!queue.isEmpty()) {
                MatchCandidate candidate = queue.poll();
                RouteTree routeTree = candidate.getNode().routes.get(segment);
                if (routeTree != null) {
                    nextLevel.add(new MatchCandidate(routeTree, new HashMap<>(candidate.getPathVariables())));
                }
                RouteTree routeTree1 = candidate.getNode().routes.get("*");
                if (routeTree1 != null) {
                    Map<String, String> pathVariables = new HashMap<>(candidate.getPathVariables());
                    pathVariables.put(routeTree1.path, segment);
                    nextLevel.add(new MatchCandidate(routeTree1, pathVariables));
                }
            }
            queue = nextLevel;
        }
        for (MatchCandidate matchCandidate : queue) {
            if(matchCandidate.getNode().getRequestHandler() != null){
                return new MatchResult(matchCandidate.getNode().getRequestHandler(), matchCandidate.getPathVariables());
            }
        }
        return null;
    }

    public void removeRoute(String segment){
        routes.remove(segment);
    }

    @Override
    public String toString() {
        return "RouteTree{" +
                "segment='" + segment + '\'' +
                ", routes=" + routes +
                ", requestHandler=" + requestHandler +
                '}';
    }
}
