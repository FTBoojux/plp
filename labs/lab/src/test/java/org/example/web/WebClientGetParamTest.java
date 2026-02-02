package org.example.web;

import framework.Best;
import framework.SimpleTestRunner;
import org.example.web.handlers.HealthCheckHandler;
import org.example.web.handlers.PathVariableHandler;
import org.example.web.handlers.QuestionParamHandler;
import org.example.web.utils.http.HttpRequestTemplates;
import org.example.web.utils.http.HttpRequestUtil;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WebClientGetParamTest {
    public static void main(String[] args) {
        new SimpleTestRunner().runAllTests(new WebClientGetParamTest());
    }
    @Best
    public void testSendRequestWithQuestionParam() throws IOException, InterruptedException {
        WebClient webClient;
        webClient = WebClient
                .build()
                .bind(8000)
                .addHandler(new HealthCheckHandler())
                .addHandler(new QuestionParamHandler());
        Thread thread = new Thread(() -> {
            try {
                webClient.listen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
        HttpRequest httpRequest = HttpRequestTemplates.baseGetRequestBuilder("http://127.0.0.1:8000/pageQuery?page=1&size=10")
                .build();
        HttpResponse<String> httpResponse = HttpRequestUtil.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(httpResponse.body());
        assert httpResponse.statusCode() == 200;
        thread.interrupt();
        webClient.close();
    }
    @Best
    public void sendRequestWithPathVariables() throws IOException, InterruptedException {
        WebClient webClient;
        webClient = WebClient
                .build()
                .bind(8001)
                .addHandler(new PathVariableHandler());
        Thread thread = new Thread(() -> {
            try {
                webClient.listen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
        HttpRequest httpRequest = HttpRequestTemplates.baseGetRequestBuilder("http://127.0.0.1:8001/hello,world!/path").build();
        HttpResponse<String> httpResponse = HttpRequestUtil.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(httpResponse.body());
        assert httpResponse.statusCode() == 200;
        thread.interrupt();
        webClient.close();
    }
}
