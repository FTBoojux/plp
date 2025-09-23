package org.example.web.middleware;

import framework.FTest;
import framework.FtAssert;
import framework.SimpleTestRunner;
import org.example.web.WebClient;

import java.io.IOException;

public class MiddlewareTest {
    public static void main(String[] args) {
        new SimpleTestRunner().runAllTests(new MiddlewareTest());
    }
    @FTest
    public void addPreMiddlewareToWebClient() throws IOException {
        ExampleMiddleware middleware = new ExampleMiddleware();
        WebClient client = WebClient.build().bind(8000)
                .addPreMiddleware(middleware);
        FtAssert.fAssert(client.getPreMiddlewares().contains(middleware));
    }
    @FTest
    public void addPostMiddlewareToWebClient() {
        ExampleMiddleware middleware = new ExampleMiddleware();
        WebClient client = WebClient.build().bind(8000)
                .addPostHandler(middleware);
        FtAssert.fAssert(client.getPostMiddlewares().contains(middleware));
    }
    @FTest
    public void middlewareCanPassRequestToNext() throws InterruptedException {
        ExampleMiddleware middleware = new ExampleMiddleware();
        AnotherMiddleware anotherMiddleware = new AnotherMiddleware();
        WebClient webClient = WebClient.build().bind(8000)
                .addPreMiddleware(middleware).addPreMiddleware(anotherMiddleware);
        new Thread(() -> {
            try {
                webClient.listen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(1000);

    }
}

class ExampleMiddleware implements MiddlewareHandler {
    @Override
    public boolean handle() {
        return true;
    }
}

class AnotherMiddleware implements MiddlewareHandler {

    @Override
    public boolean handle() {
        return true;
    }
}