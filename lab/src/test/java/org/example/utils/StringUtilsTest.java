package org.example.utils;

import framework.Best;
import framework.Bassert;
import framework.SimpleTestRunner;

public class StringUtilsTest {
    public static void main(String[] args) {
        new SimpleTestRunner().runAllTests(new StringUtilsTest());
    }
    @Best
    public void testGenerateRandomString(){
        for (int i = 0; i < 10; i++) {
            String s = StringUtils.randomString();
            Bassert.fAssert(!s.isBlank());
            System.out.println(s);
        }
    }
}
