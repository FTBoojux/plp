package org.example.web.request;

import framework.FTest;
import framework.FtAssert;
import framework.SimpleTestRunner;
import org.example.utils.StringUtils;
import org.example.web.request.cookies.Cookie;

public class CookiesTest {
    public static void main(String[] args) {
        new SimpleTestRunner().runAllTests(new CookiesTest());
    }
    @FTest
    public void addCookie(){
        Cookie cookie = new Cookie();
        cookie.add("cookie1","hello");
    }
    @FTest
    public void getCookie(){
        Cookie cookie = new Cookie();
        cookie.add("cookie1","hello");
        FtAssert.fAssert("hello".equals(cookie.get("cookie1")));
    }
    @FTest
    public void createCookiesFromRawString(){
        String headerCookie = "cookie1=hello;cookie2=Boojux";
        Cookie cookie = new Cookie(headerCookie);
        FtAssert.fAssert(StringUtils.equals(cookie.get("cookie1"),"hello"),String.format("cookie1 should be 'hello' ! get : %s", cookie.get("cookie1")));
        FtAssert.fAssert(StringUtils.equals(cookie.get("cookie2"), "Boojux"),String.format("cookie2 should be 'Boojux' ! get: %s", cookie.get("cookie2")));
    }
    @FTest
    public void parseCookieInHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.put("Cookie","cookie1=hello;cookie2=Boojux");
        Cookie cookie = headers.cookie();
        FtAssert.fAssert(StringUtils.equals(cookie.get("cookie1"),"hello"),String.format("cookie1 should be 'hello' ! get : %s", cookie.get("cookie1")));
        FtAssert.fAssert(StringUtils.equals(cookie.get("cookie2"), "Boojux"),String.format("cookie2 should be 'Boojux' ! get: %s", cookie.get("cookie2")));

    }
}
