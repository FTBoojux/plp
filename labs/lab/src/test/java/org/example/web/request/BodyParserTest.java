package org.example.web.request;

import org.example.handlers.*;
import org.example.web.WebClient;
import org.example.web.handlers.HealthCheckHandler;
import org.example.web.handlers.IdentityCheckHandler;
import org.example.web.handlers.PathVariableHandler;
import org.example.web.handlers.QuestionParamHandler;

import java.io.IOException;

public class BodyParserTest {
    public static void main(String[] args) throws IOException {
        WebClient.build()
                .bind(8000)
                .addHandler(new HealthCheckHandler())
                .addHandler(new QuestionParamHandler())
                .addHandler(new PathVariableHandler())
                .addHandler(new IdentityCheckHandler())
                .addHandler(new RequestMapHandler())
                .addHandler(new LoginHandler())
                .addHandler(new FormDataHandler())
                .addHandler(new FormurlencodedHandler())
                .listen();
    }
}
