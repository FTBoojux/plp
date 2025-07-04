package org.example.web.utils;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseBuilder {
    private String httpVersion =  "HTTP/1.1";
    private int statusCode = 200;
    private String reasonPhrase = "OK";
    private Map<String, String> headers;
    private String body;

    public HttpResponseBuilder httpVersion(String httpVersion){
        this.httpVersion = httpVersion;
        return this;
    }
    public HttpResponseBuilder statusCode(int statusCode){
        this.statusCode = statusCode;
        return this;
    }
    public HttpResponseBuilder reasonPhrase(String reasonPhrase){
        this.reasonPhrase = reasonPhrase;
        return this;
    }

    public HttpResponseBuilder body(String body){
        this.body = body;
        return this;
    }
    public HttpResponseBuilder addHeader(String key, String value){
        if(headers == null){
            headers = new HashMap<>();
        }
        headers.put(key, value);
        return this;
    }
    public HttpResponseBuilder addHeaders(Map<String, String> headers){
        if(this.headers == null){
            this.headers = new HashMap<>();
        }
        this.headers.putAll(headers);
        return this;
    }
    public String build(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.httpVersion)
                .append(" ")
                .append(this.statusCode)
                .append(" ")
                .append(this.reasonPhrase);
        sb.append("\r\n");

        if(this.headers != null){
            for(Map.Entry<String, String> entry : this.headers.entrySet()){
                sb.append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue())
                        .append("\r\n");
            }
        }
        if(this.body != null){
            sb.append("\r\n").append(this.body);
        }
        return sb.toString();

    }

}
