package org.example.web;

import framework.Best;
import org.example.handlers.HelloWorldHandler;
import org.example.handlers.LoginHandler;
import org.example.handlers.TimeOutHandler;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WebClientTimeoutTest {
    public static void main(String[] args) throws IOException {
        WebClient.build()
                .bind(8000)
                .timeOut(5, TimeUnit.SECONDS)
                .addHandler(new TimeOutHandler())
                .addHandler(new LoginHandler())
                .addHandler(new HelloWorldHandler())
                .listen();
    }
    @Best
    public void launchWithTimeOut(){

    }
}
