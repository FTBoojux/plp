package org.example.web.biew;

import framework.Bassert;
import framework.Best;
import framework.SimpleTestRunner;
import org.example.utils.StringUtils;

public class BiewTest {
    public static void main(String[] args) {
        new SimpleTestRunner().runAllTests(new BiewTest());
    }
    @Best
    public void createBiew(){
        Biew biew = new Biew("home.html");
    }
    @Best
    public void parseBiew(){
        Biew biew = new Biew("home.html");
        String page = biew.parse();
        String content = """
                <html lang="en">\r
                <head>\r
                    <title>Hello!</title>\r
                </head>\r
                <body>\r
                <div>hello!</div>\r
                </body>\r
                </html>
                """;
        Bassert.fAssert(StringUtils.equals(content.trim(), page.trim()),"Parsed content does not equal to the target content!");
    }
}
