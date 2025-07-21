package org.example.web;

import framework.FTest;
import framework.FtAssert;
import framework.SimpleTestRunner;
import org.example.web.utils.http.HttpRequestTemplates;
import org.example.web.utils.http.HttpRequestUtil;
import org.example.web.utils.web.handlers.GoodInfoHandler;
import org.example.web.utils.web.handlers.UserSpaceHandler;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WebClientPathVariablesTest {
    public static void main(String[] args) {
        new SimpleTestRunner().runAllTests(new WebClientPathVariablesTest());
    }
    @FTest
    public void testSamePrefixUrl() throws IOException, InterruptedException {
        WebClient webClient = WebClient.build()
                        .bind(8000)
                        .addHandler(new GoodInfoHandler())
                        .addHandler(new UserSpaceHandler());
        Thread thread = new Thread(() -> {
            try {
                webClient.listen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
        Thread.sleep(1000);
        HttpRequest spaceRequest = HttpRequestTemplates.baseGetRequestBuilder("http://127.0.0.1:8000/Boojux/space").build();
        HttpResponse<String> spaceResponse = HttpRequestUtil.httpClient.send(spaceRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(spaceResponse.body());
        FtAssert.fAssert(spaceResponse.statusCode() == 200);
        FtAssert.fAssert(spaceResponse.body().equals("username=Boojux"));

        HttpRequest goodInfoRequest = HttpRequestTemplates.baseGetRequestBuilder("http://127.0.0.1:8000/iron/goodInfo").build();
        HttpResponse<String> goodInfoResponse = HttpRequestUtil.httpClient.send(goodInfoRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(goodInfoResponse.body());
        FtAssert.fAssert(goodInfoResponse.statusCode() == 200);
        FtAssert.fAssert(goodInfoResponse.body().equals("goodId:iron"));

        thread.interrupt();
        webClient.close();
    }
}
