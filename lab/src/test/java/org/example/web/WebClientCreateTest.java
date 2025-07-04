package org.example.web;

import org.example.web.exceptions.PortUsedException;

public class WebClientCreateTest {
    private WebClient webClient;
    public static void main(String[] args) {
        WebClientCreateTest webClientCreateTest = new WebClientCreateTest();
        webClientCreateTest.testAll();
    }
    public void testAll(){
        this.createWebClientWithUnusedPort();
        this.createWebClientWithUsedPort();
    }
    private void createWebClientWithUnusedPort(){
        WebClient client = WebClient.build().bind(8000);
        assert client != null;
        client.close();
    }
    private void createWebClientWithUsedPort(){
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
