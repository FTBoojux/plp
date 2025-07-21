package org.example.web.utils.web;

import GlobalEnums.StringEnums;
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
//    private final String segment;
    private final boolean isWildcard;
    private final String path;
    private final Map<String, RouteTree> staticRoutes;

    public Map<String, RouteTree> getDynamicRoutes() {
        return dynamicRoutes;
    }

    public String getPath() {
        return path;
    }

    public boolean isWildcard() {
        return isWildcard;
    }

    private final Map<String, RouteTree> dynamicRoutes;


    private RequestHandler requestHandler;
    @Deprecated
    public RouteTree(String segment){
        this.staticRoutes = new HashMap<>();
        this.dynamicRoutes = new HashMap<>();
        this.path = null;
        this.requestHandler = null;
        this.isWildcard = false;
    }
    public RouteTree(String path, boolean wildcard){
        this.staticRoutes = new HashMap<>();
        this.dynamicRoutes = new HashMap<>();
        this.path = path;
        this.requestHandler = null;
        this.isWildcard = wildcard;
    }
    private RouteTree(String segment, String path){
        this.path = path;
        this.staticRoutes = new HashMap<>();
        this.dynamicRoutes = new HashMap<>();
        this.requestHandler = null;
        this.isWildcard = false;
    }
    public static RouteTree parse(String path){
        return  new RouteTree("*",path);
    }

    public Map<String, RouteTree> getStaticRoutes() {
        return staticRoutes;
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
        staticRoutes.put(segment, route);
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
            Map<String, RouteTree> routes = node.staticRoutes;
            if(StringEnums.WILDCARD.getString().equals(segment)){
                routes = node.dynamicRoutes;
                segment = segments.get(i).second;
            }
            if(!routes.containsKey(segment)){
                routes.put(segment, new RouteTree(segment,segments.get(i).second));
            }
            if(i+1 == segments.size()){
                routes.get(segment).setRequestHandler(route.getRequestHandler());
                return;
            }
            node = routes.get(segment);
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
                if( null == current.getStaticRoutes() || current.getStaticRoutes().isEmpty() ){
                    continue;
                }
                RouteTree routeTree = current.getStaticRoutes().get(split[idx]);
                if(routeTree != null){
                    queue.add(routeTree);
                }
                Map<String, RouteTree> children = current.getDynamicRoutes();
                if(children != null  && !children.isEmpty()){
                    queue.addAll(children.values());
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
                RouteTree routeTree = candidate.getNode().staticRoutes.get(segment);
                if (routeTree != null) {
                    nextLevel.add(new MatchCandidate(routeTree, new HashMap<>(candidate.getPathVariables())));
                }
                Collection<RouteTree> values = candidate.getNode().dynamicRoutes.values();
                if (values != null && !values.isEmpty()) {
                    for (RouteTree value : values) {
                        Map<String, String> pathVariables = new HashMap<>(candidate.getPathVariables());
                        pathVariables.put(value.path, segment);
                        nextLevel.add(new MatchCandidate(value, pathVariables));
                    }
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
        staticRoutes.remove(segment);
    }

    @Override
    public String toString() {
        return "RouteTree{" +
                "isWildcard=" + isWildcard +
                ", path='" + path + '\'' +
                ", staticRoutes=" + staticRoutes +
                ", dynamicRoutes=" + dynamicRoutes +
                ", requestHandler=" + requestHandler +
                '}';
    }
}
