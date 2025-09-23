package org.example.web.middleware;

import framework.FTest;
import framework.FtAssert;
import framework.SimpleTestRunner;
import org.example.utils.Fog;
import org.example.web.RequestHandler;
import org.example.web.WebClient;
import org.example.web.request.HttpHeaders;
import org.example.web.utils.http.HttpRequestTemplates;
import org.example.web.utils.http.HttpRequestUtil;
import org.example.web.utils.http.HttpResponseBuilder;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MiddlewareTest {
    public static void main(String[] args) throws InterruptedException {
        ExampleMiddleware middleware = new ExampleMiddleware();
        WebClient webClient = WebClient.build().bind(8000)
                .addPreMiddleware(middleware)
                .addPreMiddleware(new SecondMiddleware())
                .addPostHandler(new ExampleAfterMiddleware())
                .addHandler(new IllegalInformationHandler())
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
        new SimpleTestRunner().runAllTests(new MiddlewareTest());
        webClient.close();
        thread.interrupt();
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
        HttpRequest request = HttpRequestTemplates
                .baseGetRequestBuilder("http://127.0.0.1:8000/middlewareCount")
                .build();
        HttpResponse<String> httpResponse = HttpRequestUtil.httpClient
                .send(request, HttpResponse.BodyHandlers.ofString());
        String response = httpResponse.body();
        int i = Integer.parseInt(response);
        FtAssert.fAssert(i==2,"middleware should modify the request!");
    }
    @FTest
    public void middlewareCanReturnTheRequest() throws InterruptedException, IOException {
        HttpRequest request = HttpRequestTemplates
                .baseGetRequestBuilder("http://127.0.0.1:8000/middlewareCount")
                .header("cnt","1")
                .build();
        HttpResponse<String> httpResponse = HttpRequestUtil.httpClient
                .send(request, HttpResponse.BodyHandlers.ofString());
        FtAssert.fAssert(httpResponse.statusCode() == 403,
                "request with cnt should be intercepted"
                );
    }
    @FTest
    public void postMiddlewareCanReturnTheRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequestTemplates
                .baseGetRequestBuilder("http://127.0.0.1:8000/illegal")
                .build();
        HttpResponse<String> httpResponse = HttpRequestUtil.httpClient
                .send(request, HttpResponse.BodyHandlers.ofString());
        FtAssert.fAssert(httpResponse.statusCode() == 401,
                "response with illegal information should be intercepted"
        );
    }
}

class ExampleMiddleware implements MiddlewareHandler {
    @Override
    public boolean handle(org.example.web.request.HttpRequest<?> httpRequest, HttpResponseBuilder response) {
        HttpHeaders headers = httpRequest.getHeaders();
        if (headers.containsKey("cnt")) {
            response.statusCode(403).reasonPhrase("403 Forbidden");
            Fog.FOGGER.log("cnt detected!");
            return false;
        }else {
            headers.put("cnt","1");
        }
        return true;
    }
}
class SecondMiddleware implements MiddlewareHandler {

    @Override
    public boolean handle(org.example.web.request.HttpRequest<?> httpRequest, HttpResponseBuilder response) {
        HttpHeaders headers = httpRequest.getHeaders();
        if (!headers.containsKey("cnt")){
            return false;
        }
        headers.put("cnt", String.valueOf(Integer.parseInt(headers.get("cnt"))+1));
        return true;
    }
}
class ExampleAfterMiddleware implements MiddlewareHandler {

    @Override
    public boolean handle(org.example.web.request.HttpRequest<?> httpRequest, HttpResponseBuilder response) {
        if (response.getBody().contains("illegal information")) {
            response.statusCode(401)
                    .body("Unavailable For Legal Reasons");
            return false;
        }
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

class IllegalInformationHandler implements RequestHandler<Void> {

    @Override
    public Object doHandle(org.example.web.request.HttpRequest<Void> httpRequest) {
        return "illegal information";
    }

    @Override
    public String getUrl() {
        return "/illegal";
    }
}