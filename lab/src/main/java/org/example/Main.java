package org.example;

import org.example.web.WebClient;
import org.example.web.handlers.HealthCheckHandler;
import org.example.web.handlers.PathVariableHandler;
import org.example.web.handlers.QuestionParamHandler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
                WebClient.build()
                .bind(8000)
                .addHandler(new HealthCheckHandler())
                .addHandler(new QuestionParamHandler())
                .addHandler(new PathVariableHandler())
                        .listen();
    }
}