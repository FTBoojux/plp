package org.example.web;

import framework.Best;
import framework.SimpleTestRunner;
import org.example.web.exceptions.PortUsedException;

public class WebClientCreateTest {
    public static void main(String[] args) {
        WebClientCreateTest webClientCreateTest = new WebClientCreateTest();
        new SimpleTestRunner().runAllTests(webClientCreateTest);
    }
    @Best
    public void createWebClientWithUnusedPort(){
        WebClient client = WebClient.build().bind(8000);
        assert client != null;
        client.close();
    }
    @Best
    public void testCreateWebClientWithUsedPort(){
        int port = 8001;
        boolean catched = false;
        WebClient clientWithUnusedPort = WebClient.build().bind(port);
        try{
            WebClient clientWithUsedPort = WebClient.build().bind(port);
        } catch (PortUsedException pue){
            catched = true;
        } finally {
            clientWithUnusedPort.close();
        }
        assert catched;

    }
}
