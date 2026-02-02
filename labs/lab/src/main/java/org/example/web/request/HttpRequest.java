package org.example.web.request;

import org.example.enums.HTTPHeadersEnum;
import org.example.web.request.cookies.Cookie;

import java.util.HashMap;

public class HttpRequest<T> {
    private String method;
    private String path;
    private String version;
    private HttpHeaders headers;
    private HashMap<String,String> params;
    private HashMap<String,String> pathVariables;
    private FormData formData;
    private Cookie cookie;
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
                ", formData=" + formData +
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

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
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

    public FormData getFormData() {
        return formData;
    }
    public void setFormData(FormData formData) {
        this.formData = formData;
    }
    public Cookie getCookie() {
        String cookie = headers.getByEnum(HTTPHeadersEnum.COOKIE);
        this.cookie = new Cookie(cookie);
        return this.cookie;
    }

}
