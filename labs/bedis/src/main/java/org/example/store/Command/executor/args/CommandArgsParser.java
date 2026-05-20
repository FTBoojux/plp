package org.example.store.Command.executor.args;

import org.example.store.Command.executor.Command;
import org.example.util.StringUtils;

import java.util.Collections;
import java.util.List;

public final class CommandArgsParser {
    public static CommandArgs parse(List<String> args){
        if(args.isEmpty()){
            throw new IllegalArgumentException("args is empty");
        }
        String operation = args.getFirst();
        if(StringUtils.isEmpty(operation)){
            throw new IllegalArgumentException("operation is empty");
        }
        Command command = Command.valueOf(operation);
        return switch (command) {
            case GET -> newGetCommand(args);
            case SET -> newSetCommand(args);
            default -> throw new IllegalArgumentException("unknown command: " + operation);
        };
    }

    private static CommandArgs newSetCommand(List<String> args) {
        if(args.size() < 3){
            throw new IllegalArgumentException("args size is less than 3");
        }
        String key = args.get(1);
        String value = args.get(2);
        return new SetCommandArgs(key, value);
    }

    private static GetCommandArgs newGetCommand(List<String> args) {
        if(args.size() < 2) {
            throw new IllegalArgumentException("require args: `key` for get command");
        }
        return new GetCommandArgs(args.get(1));
    }
}
