package org.example.web.request;

import java.util.HashMap;

public class HttpRequest<T> {
    private String method;
    private String path;
    private String version;
    private HashMap<String,String> headers;
    private HashMap<String,String> params;
    private HashMap<String,String> pathVariables;
    private T body;

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", version='" + version + '\'' +
                ", headers=" + headers +
                ", params=" + params +
                ", pathVariables=" + pathVariables +
                ", body=" + body +
                '}';
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
    public void addHeader(String key, String value){
        this.headers.put(key,value);
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }
    public HashMap<String, String> getPathVariables() {
        return pathVariables;
    }

    public void setPathVariables(HashMap<String, String> pathVariables) {
        this.pathVariables = pathVariables;
    }
}
