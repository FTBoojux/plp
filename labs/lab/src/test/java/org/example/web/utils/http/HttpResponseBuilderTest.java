package org.example.web.utils.http;

import org.example.web.request.HttpHeaders;

public class HttpResponseBuilderTest {
    public static void main(String[] args) {
        HttpResponseBuilder responseBuilder = new HttpResponseBuilder();
        HttpHeaders headers = new HttpHeaders();
        headers.put("header1","value");
        responseBuilder.addHeaders(headers);
        System.out.println(responseBuilder.getHeaders());
    }
}
