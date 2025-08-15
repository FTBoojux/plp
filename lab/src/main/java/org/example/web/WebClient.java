package org.example.web;

import org.example.enums.HTTPHeaders;
import org.example.utils.Fog;
import org.example.utils.StringUtils;
import org.example.web.exceptions.*;
import org.example.web.request.HttpRequest;
import org.example.web.utils.Pair;
import org.example.web.utils.http.HttpResponseBuilder;
import org.example.utils.json.Bson;
import org.example.utils.json.TypeReference;
import org.example.web.utils.web.MatchResult;
import org.example.web.utils.web.PathType;
import org.example.web.utils.web.RoutePattern;
import org.example.web.utils.web.RouteTree;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

public class WebClient {
    private static final String BLANK_STRING = "";

    private Map<String, Map<String,RequestHandler>> handlers;
    private Map<Class<?>, TypeReference> requestBodyClzMap;

    private RouteTree routeTree;
    private ServerSocket socket;
    private int port;
    private int backLog = 200;
    private long timeout = 60;
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    private static final Bson bson = new Bson();

    private ExecutorService threadPool;
    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }
    private void setHandlers(Map<String, Map<String,RequestHandler>> handlers) {
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
        webClient.setRequestBodyClzMap(new HashMap<>());
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
    public WebClient timeOut(long timeout, TimeUnit timeUnit) {
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        return this;
    }
    public WebClient addHandler(RequestHandler handler){
        if(handlers.containsKey(handler.getUrl())){
            throw new DuplicatedUrlException(handler.getUrl());
        }
        RoutePattern parse = RoutePattern.parse(handler.getUrl());
        if(parse.getPathType() == PathType.STATIC){
            Map<String, RequestHandler> handlerMap = this.handlers.getOrDefault(handler.getUrl(), new HashMap<>());
            handlerMap.put(handler.getType(), handler);
            this.handlers.put(handler.getUrl(),handlerMap);
        }else{
            parse.setRequestHandler(handler);
            this.routeTree.addRoute(parse);
        }
        TypeReference requestClass = getRequestClass(handler);
        this.requestBodyClzMap.put(handler.getClass(), requestClass);
        return this;
    }
    public void listen() throws IOException {
        this.socket = new ServerSocket(this.port, this.backLog);
        System.out.println("listen on port:" + this.port);
        while(true){
            Socket accept = socket.accept();
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                        try {
                            handleRequest(accept);
                        } catch (IOException e) {
                            Fog.FOGGER.log(e.getMessage());
                        }
                    },
                    threadPool
            );
            try {
                completableFuture.get(this.timeout, this.timeUnit);
            } catch (TimeoutException e) {
                Fog.FOGGER.log("One request is canceled because of timeout!");
                String timeout = new HttpResponseBuilder()
                        .statusCode(408)
                        .reasonPhrase("Server Timeout")
                        .body("408 Request Timeout")
                        .build();
                accept.getOutputStream().write(timeout.getBytes());
                accept.getOutputStream().flush();
                accept.getOutputStream().close();
                accept.close();
                completableFuture.cancel(true);
            } catch (ExecutionException | InterruptedException e) {
                Fog.FOGGER.log(e.getMessage());
            }
        }
    }
    private void handleRequest(Socket accept) throws IOException {
        OutputStream outputStream = accept.getOutputStream();
        try{
            InputStream inputStream = accept.getInputStream();
            String line;
            List<String> lines = new ArrayList<>();
            while((line = readLine(inputStream)) != null){
                lines.add(line);
                if (line.isBlank()){
                    break;
                }
            }
            HttpRequest<Object> request = convertToRequest(lines);
            String rawBody = parseRequestBody(request, inputStream);
            Fog.FOGGER.log(request);
            MatchResult matchResult = findRequestHandler(request.getMethod(),request.getPath());
//                RequestHandler requestHandler1 = getRequesthandler(request.getPath());
            if(matchResult == null || matchResult.requestHandler == null){
                String notFound = new HttpResponseBuilder()
                        .statusCode(404)
                        .reasonPhrase("Not Found")
                        .body("404 Not Found")
                        .build();
                outputStream.write(notFound.getBytes());
            }else{
                request.setPathVariables(matchResult.pathVariables);
                RequestHandler requestHandler = matchResult.requestHandler;

                parseJsonBody(requestHandler, rawBody, request);
                Object responseBody = requestHandler.doHandle(request);
                HttpResponseBuilder responseBuilder = new HttpResponseBuilder()
                        .statusCode(200)
                        .reasonPhrase("OK");
                if(!Objects.isNull(responseBody)){
                    responseBuilder.body(responseBody.toString());
                }else{
                    responseBuilder.body("");
                }
                String response =
                        responseBuilder
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

    private void parseJsonBody(RequestHandler requestHandler, String rawBody, HttpRequest<Object> request) {
        TypeReference typeReference = this.requestBodyClzMap.get(requestHandler.getClass());
        if(typeReference.getType() != Void.class && !StringUtils.isEmpty(rawBody)){
            Object requestBody = Bson.deserializeFromJson(rawBody, typeReference);
            request.setBody(requestBody);
        }
    }

    private static String parseRequestBody(HttpRequest<Object> request, InputStream inputStream) throws IOException {
        HashMap<String, String> headers = request.getHeaders();
        String number = headers.getOrDefault(HTTPHeaders.CONTENT_LENGTH.getHeader(), "0");
        int contentLength = Integer.parseInt(number);
        String rawBody = null;
        if(contentLength > 0){
            byte[] bodyBytes = new byte[contentLength];
            int totalBytes = 0;
            while (totalBytes < contentLength){
                int read = inputStream.read(bodyBytes, totalBytes, contentLength - totalBytes);
                if(read == -1){
                    throw new InvalidRequestBodyException("Unexpected end of stream!");
                }
                totalBytes += read;
            }
            rawBody = new String(bodyBytes, StandardCharsets.UTF_8);
            request.setBody(rawBody);
        }
        return rawBody;
    }

    private static TypeReference getRequestClass(RequestHandler requestHandler) {
//        Class<?> requestClz = Void.class;
        TypeReference typeReference = new TypeReference(Void.class);
        Type[] requestInterfaces = requestHandler.getClass().getGenericInterfaces();
        for (Type requestInterface : requestInterfaces) {
            if (requestInterface instanceof ParameterizedType){
                ParameterizedType parameterizedType = (ParameterizedType) requestInterface;
                if (parameterizedType.getRawType() == RequestHandler.class){
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    if(actualTypeArguments.length > 0){
//                        requestClz = (Class<?>) actualTypeArguments[0];
                        typeReference = new TypeReference(actualTypeArguments[0]);
                    }
                }
            }
        }
        return typeReference;
    }

    private String readLine(InputStream inputStream) throws IOException {
        ByteArrayOutputStream lineBuffer = new ByteArrayOutputStream();
        int previousByte = -1;
        int currentByte;
        while ((currentByte = inputStream.read()) != -1){
            if(previousByte == '\r' && currentByte == '\n'){
                return lineBuffer.toString(StandardCharsets.UTF_8);
            }
            lineBuffer.write(currentByte);
            previousByte = currentByte;
        }
        if (lineBuffer.size() > 0){
            return lineBuffer.toString(StandardCharsets.UTF_8);
        }
        return null;
    }

    public HashMap<String, String> parseBody(char[] bodyChars) {
        String rawBody = new String(bodyChars);
        if(!rawBody.startsWith("{") || !rawBody.endsWith("}")){
            throw new InvalidRequestBodyException("Invalid request body:"+rawBody);
        }
        rawBody = rawBody.substring(1,rawBody.length()-1);
        String[] pairStrings = rawBody.split(",");
        HashMap<String, String> requestBody = new HashMap<>();
        for (String pairString : pairStrings) {
            Pair<String, String> pair = formatPairArr(pairString);
            if(Objects.nonNull(pair)){
                requestBody.put(pair.first, pair.second);
            }
        }
        return requestBody;
    }

    private Pair<String, String> formatPairArr(String pairString) {
        if(StringUtils.isEmpty(pairString)){
            return null;
        }
        String[] pair = pairString.split(":");
        String key = pair.length > 0 ? pair[0].trim() : "";
        String value = pair.length > 1 ? pair[1].trim() : "";
        if(!key.startsWith("\"") || !key.endsWith("\"")){
            throw new InvalidRequestBodyException("Uncompleted request body! key = " + key);
        }
        if(!value.startsWith("\"") || !value.endsWith("\"")){
            throw new InvalidRequestBodyException("Uncompleted request body! value = " + key);
        }
        key = key.substring(1,key.length()-1);
        value = value.substring(1,value.length()-1);
        return new Pair<>(key, value);
    }

    private MatchResult findRequestHandler(String type,String path) {
        Map<String, RequestHandler> requestHandlerMap = this.handlers.get(path);
        RequestHandler requestHandler = null;
        if (requestHandlerMap != null){
            requestHandler = requestHandlerMap.get(type);
        }
        if(requestHandler != null){
            return new MatchResult(requestHandler, new HashMap<>(0));
        }else{
            return this.routeTree.findWithVariable(type,path);
        }
    }

    private HttpRequest convertToRequest(List<String> requestLines) {
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
                String[] split = trim.split(": ");
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

    public Map<Class<?>, TypeReference> getRequestBodyClzMap() {
        return requestBodyClzMap;
    }

    public void setRequestBodyClzMap(Map<Class<?>, TypeReference> requestBodyClzMap) {
        this.requestBodyClzMap = requestBodyClzMap;
    }
}
