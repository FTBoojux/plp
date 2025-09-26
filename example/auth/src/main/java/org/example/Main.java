package org.example;

import org.example.handlers.FrontPageHandler;
import org.example.handlers.PrivateHandler;
import org.example.interceptors.AuthInterceptor;
import org.example.web.WebClient;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        WebClient.build().bind(8000)
                .addPreMiddleware(new AuthInterceptor())
                .addHandler(new FrontPageHandler())
                .addHandler(new PrivateHandler())
                .listen();
    }
}