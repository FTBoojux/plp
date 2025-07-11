package org.example.web.utils.http;
import java.net.URI;
import java.net.http.HttpRequest;

public class HttpRequestTemplates {

    public static HttpRequest.Builder baseGetRequestBuilder(String baseUrl) {
        return HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .GET()
                .header("Accept", "application/json"); // Common header
    }

    public static HttpRequest.Builder basePostRequestBuilder(String baseUrl) {
        return HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json"); // Common header for POST
    }
}