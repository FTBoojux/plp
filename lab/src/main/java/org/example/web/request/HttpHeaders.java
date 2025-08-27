package org.example.web.request;

import org.example.enums.HTTPHeadersEnum;

import java.util.HashMap;

public class HttpHeaders extends HashMap<String, String> {
    public int getContentLength() {
        String contentLengthString = this.getOrDefault(HTTPHeadersEnum.CONTENT_LENGTH.getHeader(),"0");
        return Integer.parseInt(contentLengthString);
    }
    public int getIntDefaultZero(String key) {
        String contentLengthString = this.getOrDefault(key,"0");
        return Integer.parseInt(contentLengthString);
    }
    public String getByEnum(HTTPHeadersEnum header) {
        return get(header.getHeader());
    }
}
