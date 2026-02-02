package org.example.web;

import org.example.web.handlers.HealthCheckHandler;
import org.example.web.utils.web.handlers.GoodInfoHandler;
import org.example.web.utils.web.handlers.UserSpaceHandler;

import java.io.IOException;

public class WebClientListenTest {
    public static void main(String[] args) throws IOException {
        WebClient.build()
                .bind(8000)
                .addHandler(new HealthCheckHandler())
                .addHandler(new UserSpaceHandler())
                .addHandler(new GoodInfoHandler())
                .listen();
    }
}
