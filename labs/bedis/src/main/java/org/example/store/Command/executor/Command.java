package org.example.store.Command.executor;

public enum Command{

    GET("GET"),
    SET("SET"),
    DEL("DEL"),
    INCR("INCR"),
    DECR("DECR"),
    ;
    public final String command;
    Command(String command){
        this.command = command;
    }
}