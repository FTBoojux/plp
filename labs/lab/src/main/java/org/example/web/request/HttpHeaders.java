package org.example.web.request;

import GlobalEnums.StringEnums;
import org.example.enums.HTTPHeadersEnum;
import org.example.web.request.cookies.Cookie;

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
        String result = get(header.getHeader());
        if (null == result) {
            return StringEnums.EMPTY.getString();
        } else {
            return result;
        }
    }

    public Cookie cookie() {
        return new Cookie(getByEnum(HTTPHeadersEnum.COOKIE));
    }
}
