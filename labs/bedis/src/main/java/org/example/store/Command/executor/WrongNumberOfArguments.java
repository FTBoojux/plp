package org.example.store.Command.executor;

public class WrongNumberOfArguments extends RuntimeException {
    public WrongNumberOfArguments(int expected, int got) {
        super(String.format("ERROR: Expected arguments number: %d, but got %d", expected, got));
    }
}
