package org.example.web.request.cookies;

import GlobalEnums.StringEnums;
import org.example.utils.Fog;

import java.util.HashMap;
import java.util.Map;

public class Cookie {
    public Cookie(){
        cookies = new HashMap<>();
    }
    public Cookie(String cookies) {
        Map<String, String> _cookies = new HashMap<>();
        String[] cookiePairs = cookies.split(";");
        for (String cookie : cookiePairs) {
            int equals = cookie.indexOf('=');
            if (equals == -1) {
                Fog.FOGGER.log("Invalid cookies!");
                continue;
            }
            String key = cookie.substring(0,equals);
            String value = cookie.substring(equals+1);
            _cookies.put(key,value);
        }
        this.cookies = _cookies;
    }
    Map<String, String> cookies = new HashMap<>();
    public void add(String cookie1, String value) {
        cookies.put(cookie1,value);
    }

    public String get(String key) {
        String value = cookies.get(key);
        if (value == null) {
            return StringEnums.EMPTY.getString();
        }
        return value;
    }
}
