package org.example.web.middleware;

import framework.FTest;
import framework.FtAssert;
import framework.SimpleTestRunner;
import org.example.utils.Fog;
import org.example.web.RequestHandler;
import org.example.web.WebClient;
import org.example.web.utils.http.HttpRequestTemplates;
import org.example.web.utils.http.HttpRequestUtil;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
    public void middlewareCanModifyTheRequest() throws InterruptedException, IOException {
        ExampleMiddleware middleware = new ExampleMiddleware();
        WebClient webClient = WebClient.build().bind(8000)
                .addPreMiddleware(middleware)
                .addHandler(new ExampleHandler());
        Thread thread = new Thread(() -> {
            try {
                webClient.listen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
        Thread.sleep(1000);
        HttpRequest request = HttpRequestTemplates
                .baseGetRequestBuilder("http://127.0.0.1:8000/middlewareCount")
                .build();
        HttpResponse<String> httpResponse = HttpRequestUtil.httpClient
                .send(request, HttpResponse.BodyHandlers.ofString());
        String response = httpResponse.body();
        int i = Integer.parseInt(response);
        FtAssert.fAssert(i==1,"middleware should modify the request!");
        thread.interrupt();
        webClient.close();
    }
}

class ExampleMiddleware implements MiddlewareHandler {
    @Override
    public boolean handle(org.example.web.request.HttpRequest<?> httpRequest) {
        httpRequest.getHeaders().put("cnt","1");
        Fog.FOGGER.log("hi!");
        return true;
    }
}

class ExampleHandler implements RequestHandler<Void> {

    @Override
    public Object doHandle(org.example.web.request.HttpRequest<Void> httpRequest) {
        int cnt = httpRequest.getHeaders().getIntDefaultZero("cnt");
        return String.valueOf(cnt);
    }

    @Override
    public String getUrl() {
        return "/middlewareCount";
    }
}