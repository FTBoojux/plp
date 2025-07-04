package org.example.web;

import framework.SimpleTestRunner;
import org.example.web.exceptions.DuplicatedUrlException;
import org.example.web.handlers.HealthCheckHandler;

public class WebClientLinkTest {
    public static void main(String[] args) {
        new SimpleTestRunner().runAllTests(new WebClientLinkTest());
    }
    public void testAddHandlerSuccessfully(){
        WebClient webClient = WebClient.build();
        webClient.bind(8000)
                .addHandler(new HealthCheckHandler());
        webClient.close();
    }
    public void testAddDuplicateUrlShouldFail(){
        WebClient webClient = WebClient.build();
        boolean catched = false;
        try{
            webClient.bind(8001)
                    .addHandler(new HealthCheckHandler())
                    .addHandler(new HealthCheckHandler());
        } catch (DuplicatedUrlException e){
            catched = true;
        }
        assert catched;
    }
}
