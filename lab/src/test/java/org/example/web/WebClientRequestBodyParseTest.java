package org.example.web;

import framework.Best;
import framework.Bassert;
import framework.SimpleTestRunner;
import org.example.web.exceptions.InvalidRequestBodyException;

import java.util.HashMap;

public class WebClientRequestBodyParseTest {
    public static void main(String[] args) {
        WebClientRequestBodyParseTest obj = new WebClientRequestBodyParseTest();
        new SimpleTestRunner().runAllTests(obj);
    }
    @Best
    public void validJsonRequest(){
        String json = "{\"name\":\"Boojux\",\"gender\":\"Transformers\"}";
        char[] charArray = json.toCharArray();
        WebClient webClient = new WebClient();
        HashMap<String, String> map = webClient.parseBody(charArray);
        Bassert.fAssert(map.get("name").equals("Boojux"));
        Bassert.fAssert(map.get("gender").equals("Transformers"));
    }
    @Best
    public void validExtendJson(){
        String json = "{\"name\":\"Boojux\",\"gender\":\"Transformers\",}";
        char[] charArray = json.toCharArray();
        WebClient webClient = new WebClient();
        HashMap<String, String> map = webClient.parseBody(charArray);
        Bassert.fAssert(map.get("name").equals("Boojux"));
        Bassert.fAssert(map.get("gender").equals("Transformers"));
    }
    @Best
    public void validInformalJson(){
        String json = "{\"name\":\"Boojux\",\n  \"gender\":\"Transformers\",}";
        char[] charArray = json.toCharArray();
        WebClient webClient = new WebClient();
        HashMap<String, String> map = webClient.parseBody(charArray);
        Bassert.fAssert(map.get("name").equals("Boojux"));
        Bassert.fAssert(map.get("gender").equals("Transformers"));
    }
    @Best
    public void invalidJsonShouldFail(){
        String json = "{\"name\":\"Boojux\",\n  \"gender\":\"Transformers}";
        char[] charArray = json.toCharArray();
        WebClient webClient = new WebClient();
        boolean fail = false;
        try {
            HashMap<String, String> map = webClient.parseBody(charArray);
        } catch (InvalidRequestBodyException e) {
            fail = true;
        }
        Bassert.fAssert(fail,"invalid json should fail");
    }

}
