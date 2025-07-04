package org.example.web;

import framework.SimpleTestRunner;
import org.example.web.exceptions.PortUsedException;

public class WebClientCreateTest {
    public static void main(String[] args) {
        WebClientCreateTest webClientCreateTest = new WebClientCreateTest();
        new SimpleTestRunner().runAllTests(webClientCreateTest);
    }
    public void testCreateWebClientWithUnusedPort(){
        WebClient client = WebClient.build().bind(8000);
        assert client != null;
        client.close();
    }
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
