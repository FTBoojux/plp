package org.example.web;

import framework.FTest;
import framework.SimpleTestRunner;
import org.example.handler.HelloWorldHandler;
import org.example.handler.LoginHandler;
import org.example.handler.RequestMapHandler;
import org.example.handler.TimeOutHandler;
import org.example.web.handlers.HealthCheckHandler;
import org.example.web.handlers.IdentityCheckHandler;
import org.example.web.handlers.PathVariableHandler;
import org.example.web.handlers.QuestionParamHandler;

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
    @FTest
    public void launchWithTimeOut(){

    }
}
