package org.example.web;

import org.example.web.exceptions.DuplicatedUrlException;
import org.example.web.exceptions.InvalidParamsException;
import org.example.web.exceptions.PortUsedException;
import org.example.web.exceptions.SocketCloseFailException;
import org.example.web.request.HttpRequest;
import org.example.web.utils.http.HttpResponseBuilder;
import org.example.web.utils.web.MatchResult;
import org.example.web.utils.web.PathType;
import org.example.web.utils.web.RoutePattern;
import org.example.web.utils.web.RouteTree;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebClient {
    private static final String BLANK_STRING = "";

    private Map<String, RequestHandler> handlers;

    private RouteTree routeTree;
    private ServerSocket socket;
    private int port;
    private int backLog = 200;


    private ExecutorService threadPool;
    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }
    private void setHandlers(Map<String, RequestHandler> handlers) {
        this.handlers = handlers;
    }
    public RouteTree getRouteTree() {
        return routeTree;
    }

    public void setRouteTree(RouteTree routeTree) {
        this.routeTree = routeTree;
    }
    public static WebClient build(){
        WebClient webClient = new WebClient();
        webClient.setHandlers(new HashMap<>());
        webClient.setRouteTree(new RouteTree("",true));
        webClient.setThreadPool(Executors.newCachedThreadPool());
        return webClient;
    }
    public WebClient bind(int port){
        this.port = port;
        return this;
    }
    public WebClient backLog(int backLog){
        this.backLog = backLog;
        return this;
    }
    public WebClient addHandler(RequestHandler handler){
        if(handlers.containsKey(handler.getUrl())){
            throw new DuplicatedUrlException(handler.getUrl());
        }
        RoutePattern parse = RoutePattern.parse(handler.getUrl());
        if(parse.getPathType() == PathType.STATIC){
            this.handlers.put(handler.getUrl(), handler);
        }else{
            parse.setRequestHandler(handler);
            this.routeTree.addRoute(parse);
        }
        return this;
    }
    public void listen() throws IOException {
        this.socket = new ServerSocket(this.port, this.backLog);
        System.out.println("listen on port:" + this.port);
        while(true){
            Socket accept = socket.accept();
            threadPool.execute(()->{
                try {
                    handleRequest(accept);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });
        }
    }

    private void handleRequest(Socket accept) throws IOException {
        OutputStream outputStream = accept.getOutputStream();
        try{
            InputStream inputStream = accept.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            List<String> lines = new ArrayList<>();
            while((line = bufferedReader.readLine()) != null && !line.isEmpty()){
                lines.add(line);
            }
            HttpRequest request = convertToRequest(lines);
            System.out.println(request);
            MatchResult matchResult = findRequestHandler(request.getPath());
//                RequestHandler requestHandler1 = getRequesthandler(request.getPath());
            if(matchResult == null || matchResult.requestHandler == null){
                String notFound = new HttpResponseBuilder()
                        .statusCode(404)
                        .reasonPhrase("Not Found")
                        .build();
                outputStream.write(notFound.getBytes());
            }else{
                request.setPathVariables(matchResult.pathVariables);
                Object object = matchResult.requestHandler.get(request);
                String response = new HttpResponseBuilder()
                        .statusCode(200)
                        .reasonPhrase("OK")
                        .body(object.toString())
                        .build();
                outputStream.write(response.getBytes());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            String response = new HttpResponseBuilder()
                    .statusCode(500)
                    .reasonPhrase("Error")
                    .body("500 Internal Server Error")
                    .build();
            outputStream.write(response.getBytes());

        } finally {
            outputStream.flush();
            outputStream.close();
            accept.close();
        }
    }

    private MatchResult findRequestHandler(String path) {
        RequestHandler requestHandler = this.handlers.get(path);
        if(requestHandler != null){
            return new MatchResult(requestHandler, new HashMap<>(0));
        }else{
            return this.routeTree.findWithVariable(path);
        }
    }

    private HttpRequest convertToRequest(List<String> requestLines) {
        System.out.println(requestLines);
        HttpRequest objectHttpRequest = new HttpRequest<>();
        String first = requestLines.getFirst();
        String[] firstLine = first.split(" ");
        String method = firstLine[0];
        String path = firstLine[1];
        String httpVersion = firstLine[2];
        String[] paths = path.split("\\?");
        objectHttpRequest.setMethod(method);
        objectHttpRequest.setPath(paths[0]);
        objectHttpRequest.setVersion(httpVersion);
        if(paths.length > 1){
            paths = paths[1].split("&");
            HashMap<String, String> params = new HashMap<>();
            for (String s : paths) {
                String[] keyAndValue = s.split("=");
                if (keyAndValue.length > 2) {
                    throw new InvalidParamsException("path : {" + path + "} is invalid");
                }
                params.put(keyAndValue[0], keyAndValue.length == 2 ? keyAndValue[1] : "");
            }
            objectHttpRequest.setParams(params);
        }

        int blankIndex = -1;
        HashMap<String, String> headers = new HashMap<>();
        objectHttpRequest.setHeaders(headers);
        for(int i = 1; i < requestLines.size(); i++){
            String trim = requestLines.get(i).trim();
            if(BLANK_STRING.equals(trim)){
                blankIndex = i;
                break;
            }else{
                String[] split = trim.split(":");
                headers.put(split[0], split[1]);
            }
        }
        return objectHttpRequest;
    }

    public void close(){
        try {
            this.socket.close();
            System.out.println("socket on port: " + port  + " closed");
        } catch (IOException e) {
            throw new SocketCloseFailException(e.getMessage());
        }
    }

}
