package org.example;

import org.example.handlers.CommentListHandler;
import org.example.handlers.CommentSubmitHandler;
import org.example.handlers.FrontPageHandler;
import org.example.web.WebClient;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        WebClient.build().bind(8000)
                .addHandler(new FrontPageHandler())
                .addHandler(new CommentSubmitHandler())
                .addHandler(new CommentListHandler())
                .listen();
    }
}