package org.example.web;

import java.util.Map;

public class WebClient {
    private Map<String, RequestHandler>  handlers;
    public static WebClient build(){
        return new WebClient();
    }
    public WebClient bind(int port){
       return this;
    }

}
