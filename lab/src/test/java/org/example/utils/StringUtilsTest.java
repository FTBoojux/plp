package org.example.utils;

import framework.FTest;
import framework.FtAssert;
import framework.SimpleTestRunner;

public class StringUtilsTest {
    public static void main(String[] args) {
        new SimpleTestRunner().runAllTests(new StringUtilsTest());
    }
    @FTest
    public void testGenerateRandomString(){
        for (int i = 0; i < 10; i++) {
            String s = StringUtils.randomString();
            FtAssert.fAssert(!s.isBlank());
            System.out.println(s);
        }
    }
}
