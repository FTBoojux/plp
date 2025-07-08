package org.example.web;

import org.example.web.exceptions.DuplicatedUrlException;
import org.example.web.exceptions.PortUsedException;
import org.example.web.exceptions.SocketCloseFailException;
import org.example.web.request.HttpRequest;
import org.example.web.utils.HttpResponseBuilder;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class WebClient {
    private static final String BLANK_STRING = "";
    private void setHandlers(Map<String, RequestHandler> handlers) {
        this.handlers = handlers;
    }

    private Map<String, RequestHandler> handlers;
    private ServerSocket socket;
    private int port;
    public static WebClient build(){
        WebClient webClient = new WebClient();
        webClient.setHandlers(new HashMap<>());
        return webClient;
    }
    public WebClient bind(int port){
        InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
        try {
            this.socket = new ServerSocket();
            this.socket.bind(inetSocketAddress);
            this.port = port;
            System.out.println("listening on port " + inetSocketAddress.getPort());
        } catch (IOException e) {
            throw new PortUsedException(port);
        }
        return this;
    }
    public WebClient addHandler(RequestHandler handler){
        if(handlers.containsKey(handler.getUrl())){
            throw new DuplicatedUrlException(handler.getUrl());
        }
        this.handlers.put(handler.getUrl(), handler);
        return this;
    }
    public void listen() throws IOException {
        while(true){
            Socket accept = socket.accept();
            InputStream inputStream = accept.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            List<String> lines = new ArrayList<>();
            OutputStream outputStream = accept.getOutputStream();
            while((line = bufferedReader.readLine()) != null && !line.isEmpty()){
                lines.add(line);
            }
            HttpRequest<HashMap<String, String>> request = convertToRequest(lines);
            System.out.println(request);
            String[] split = request.getPath().split("\\?");
            String path = split[0];
            RequestHandler requestHandler = handlers.get(path);
            if(requestHandler == null){
                String notFound = new HttpResponseBuilder()
                        .statusCode(404)
                        .reasonPhrase("Not Found")
                        .build();
                outputStream.write(notFound.getBytes());
            }else{
                Object object = requestHandler.get();
                String response = new HttpResponseBuilder()
                        .statusCode(200)
                        .reasonPhrase("OK")
                        .body(object.toString())
                        .build();
                outputStream.write(response.getBytes());
            }
            outputStream.flush();
            outputStream.close();
            accept.close();
        }
    }

    private HttpRequest<HashMap<String, String>> convertToRequest(List<String> requestLines) {
        System.out.println(requestLines);
        HttpRequest<HashMap<String,String>> objectHttpRequest = new HttpRequest<>();
        String first = requestLines.getFirst();
        String[] firstLine = first.split(" ");
        String method = firstLine[0];
        String path = firstLine[1];
        String httpVersion = firstLine[2];
        objectHttpRequest.setMethod(method);
        objectHttpRequest.setPath(path);
        objectHttpRequest.setVersion(httpVersion);
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
