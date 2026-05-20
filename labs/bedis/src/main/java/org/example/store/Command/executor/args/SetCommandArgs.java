package org.example.store.Command.executor.args;

public record SetCommandArgs(String key, String value) implements CommandArgs{
}
