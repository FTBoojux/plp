package org.example.web;

import framework.FTest;
import framework.SimpleRandomPathStressTest;
import framework.SimpleStressTest;
import framework.SimpleTestRunner;

public class WebClientStressTest {
    private static final String TARGET_URL = "http://127.0.0.1:8000/health";
    private static final String DYNAMIC_URL_RULE = "/{pathVariable}/path";
    private static final String LINE = "+".repeat(50);
    public static void main(String[] args) throws InterruptedException {
        WebClientStressTest clientStressTest = new WebClientStressTest();
        clientStressTest.warmUp();
        new SimpleTestRunner().runAllTests(clientStressTest);

    }

    private void warmUp() throws InterruptedException {
        System.out.println(LINE);
        System.out.println("+".repeat(24)+"预热"+"*".repeat(24));
        new SimpleStressTest(TARGET_URL,
                100,
                Runtime.getRuntime().availableProcessors() / 2
        ).runTest();
        System.out.println("+".repeat(23)+"预热结束"+"*".repeat(23));
        System.out.println(LINE);
    }
    @FTest
    public void testDynamicPath() throws InterruptedException {
        WebClientStressTest clientStressTest = new WebClientStressTest();
        for(int i = 10; i <= 100; i+=10){
            clientStressTest.runDynamicPath(i);
            System.out.println(LINE);
        }
    }
//    @FTest
    public void testStaticPath() throws InterruptedException {
        WebClientStressTest clientStressTest = new WebClientStressTest();
        for(int i = 10; i <= 100; i+=10){
            clientStressTest.runStaticPathStressTest(i);
            System.out.println(LINE);
        }
    }
    private void runDynamicPath(int i) throws InterruptedException {
        new SimpleRandomPathStressTest(
            DYNAMIC_URL_RULE,
            i,
            Runtime.getRuntime().availableProcessors()/2
        ).runTest();
    }
    private void runStaticPathStressTest(int i) throws InterruptedException {
        new SimpleStressTest(
                TARGET_URL,
                i,
                Runtime.getRuntime().availableProcessors()
        ).runTest();
    }
}
