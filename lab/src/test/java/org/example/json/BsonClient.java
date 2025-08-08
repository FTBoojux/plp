package org.example.json;

import org.example.handler.LoginHandler;
import org.example.handler.RequestMapHandler;
import org.example.web.WebClient;
import org.example.web.handlers.HealthCheckHandler;
import org.example.web.handlers.IdentityCheckHandler;
import org.example.web.handlers.PathVariableHandler;
import org.example.web.handlers.QuestionParamHandler;

import java.io.IOException;

public class BsonClient {
    public static void main(String[] args) throws IOException {
        WebClient.build()
                .bind(8000)
                .addHandler(new HealthCheckHandler())
                .addHandler(new QuestionParamHandler())
                .addHandler(new PathVariableHandler())
                .addHandler(new IdentityCheckHandler())
                .addHandler(new RequestMapHandler())
                .addHandler(new LoginHandler())
                .listen();
    }
}
