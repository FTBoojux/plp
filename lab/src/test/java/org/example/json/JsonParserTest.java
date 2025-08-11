package org.example.json;

import framework.FTest;
import framework.SimpleTestRunner;
import org.example.utils.json.JsonParser;

public class JsonParserTest {
    public static void main(String[] args) {
        JsonParserTest jsonParserTest = new JsonParserTest();
        new SimpleTestRunner().runAllTests(jsonParserTest);
    }
    @FTest
    public void parseJson(){
        String json = """
{
  "id": "12\\n3",
  "price": "123.45",
  "quantity": -10,
  "ratio": 1.2e-5,
  "nullable": null,
  "na_me": "na_me"
}""";
        Object parse = new JsonParser().parse(json);
        System.out.println(parse);
    }
}
