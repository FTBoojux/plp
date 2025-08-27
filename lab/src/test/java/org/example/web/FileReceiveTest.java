package org.example.web;

import org.example.handler.*;
import org.example.web.handlers.HealthCheckHandler;
import org.example.web.handlers.IdentityCheckHandler;
import org.example.web.handlers.PathVariableHandler;
import org.example.web.handlers.QuestionParamHandler;

import java.io.IOException;

public class FileReceiveTest {
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
                .addHandler(new FileUploadHandler())
                .listen();
    }
}
