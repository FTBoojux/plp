package org.example.web;

public interface RequestHandler {
    default Object get(){
        String msg = String.join("", "The implement: ", getUrl(), "have not be implemented yet!");
        throw new RuntimeException(msg);
    }
    String getUrl();
}
