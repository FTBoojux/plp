package org.example.web;

import org.example.web.handlers.HealthCheckHandler;

import java.io.IOException;

public class WebClientListenTest {
    public static void main(String[] args) throws IOException {
        WebClient.build()
                .bind(8000)
                .addHandler(new HealthCheckHandler())
                .listen();
    }
}
