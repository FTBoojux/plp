package org.example.web;

import framework.FTest;
import framework.FtAssert;
import framework.SimpleTestRunner;
import org.example.utils.StringUtils;
import org.example.web.request.HttpRequest;
import org.example.web.request.cookies.Cookie;
import org.example.web.utils.http.HttpRequestTemplates;
import org.example.web.utils.http.HttpRequestUtil;

import java.io.IOException;
import java.net.http.HttpResponse;

public class WebClientCookieTest {
    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            try {
                WebClient.build().bind(8000)
                        .addHandler(new CookiesHandler())
                        .listen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(1000);
        new SimpleTestRunner().runAllTests(new WebClientCookieTest());
    }
    @FTest
    public void requestWithCookie() {
        java.net.http.HttpRequest request = HttpRequestTemplates.baseGetRequestBuilder("http://127.0.0.1:8000/cookie")
                .header("Cookie", "cnt=1")
                .build();
        try {
            HttpResponse<String> response = HttpRequestUtil.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            FtAssert.fAssert(StringUtils.equals(response.body(),"1"), "cnt returned should equal to 1!");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class CookiesHandler implements RequestHandler<Void> {

    @Override
    public Object doHandle(HttpRequest<Void> httpRequest) {
        Cookie cookie = httpRequest.getCookie();
        return cookie.get("cnt");
    }

    @Override
    public String getUrl() {
        return "/cookie";
    }
}