package org.example.web;

import framework.SimpleTestRunner;
import org.example.web.handlers.HealthCheckHandler;
import org.example.web.handlers.QuestionParamHandler;

import java.io.IOException;

public class WebClientGetParamTest {
    public static void main(String[] args) {
        new SimpleTestRunner().runAllTests(new WebClientGetParamTest());
    }
    public void testSendRequestWithQuestionParam(){
//        new Thread(()->{
            try {
                WebClient.build()
                        .bind(8000)
                        .addHandler(new HealthCheckHandler())
                        .addHandler(new QuestionParamHandler())
                        .listen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//        }).start();

    }
}
