package org.example.web;

import org.example.web.exceptions.PortUsedException;
import org.example.web.exceptions.SocketCloseFailException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;

public class WebClient {
    private Map<String, RequestHandler>  handlers;
    private Socket socket;
    private int port;
    public static WebClient build(){
        Socket socket = new Socket();
        WebClient webClient = new WebClient();
        webClient.setSocket(socket);
        return webClient;
    }
    public WebClient bind(int port){
        InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
        try {
            this.socket.bind(inetSocketAddress);
            this.port = port;
            System.out.println("listening on port " + inetSocketAddress.getPort());
        } catch (IOException e) {
            throw new PortUsedException(port);
        }
        return this;
    }
    public void close(){
        try {
            this.socket.close();
            System.out.println("socket on port: " + port  + " closed");
        } catch (IOException e) {
            throw new SocketCloseFailException(e.getMessage());
        }
    }
    private void setSocket(Socket socket){
        this.socket = socket;
    }

}
