package org.example.web;

import org.example.web.exceptions.DuplicatedUrlException;
import org.example.web.exceptions.PortUsedException;
import org.example.web.exceptions.SocketCloseFailException;
import org.example.web.utils.HttpResponseBuilder;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebClient {

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
            String requestLine = lines.getFirst();
            String[] requestLines = requestLine.split(" ");
            System.out.println(requestLine);
            RequestHandler requestHandler = handlers.get(requestLines[1]);
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
//            accept.close();
        }
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
