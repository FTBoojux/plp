package org.example.web.request;

import framework.Best;
import framework.Bassert;
import framework.SimpleTestRunner;
import org.example.utils.StringUtils;
import org.example.web.request.cookies.Cookie;

public class CookiesTest {
    public static void main(String[] args) {
        new SimpleTestRunner().runAllTests(new CookiesTest());
    }
    @Best
    public void addCookie(){
        Cookie cookie = new Cookie();
        cookie.add("cookie1","hello");
    }
    @Best
    public void getCookie(){
        Cookie cookie = new Cookie();
        cookie.add("cookie1","hello");
        Bassert.fAssert("hello".equals(cookie.get("cookie1")));
    }
    @Best
    public void createCookiesFromRawString(){
        String headerCookie = "cookie1=hello;cookie2=Boojux";
        Cookie cookie = new Cookie(headerCookie);
        Bassert.fAssert(StringUtils.equals(cookie.get("cookie1"),"hello"),String.format("cookie1 should be 'hello' ! get : %s", cookie.get("cookie1")));
        Bassert.fAssert(StringUtils.equals(cookie.get("cookie2"), "Boojux"),String.format("cookie2 should be 'Boojux' ! get: %s", cookie.get("cookie2")));
    }
    @Best
    public void parseCookieInHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.put("Cookie","cookie1=hello;cookie2=Boojux");
        Cookie cookie = headers.cookie();
        Bassert.fAssert(StringUtils.equals(cookie.get("cookie1"),"hello"),String.format("cookie1 should be 'hello' ! get : %s", cookie.get("cookie1")));
        Bassert.fAssert(StringUtils.equals(cookie.get("cookie2"), "Boojux"),String.format("cookie2 should be 'Boojux' ! get: %s", cookie.get("cookie2")));

    }
}
