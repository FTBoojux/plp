package org.example.store.Command;

import org.example.store.Command.executor.Command;
import org.example.store.Command.executor.GetExecutor;
import org.example.store.Command.executor.SetExecutor;
import org.example.store.engine.StoreEngine;

import java.util.HashMap;
import java.util.Map;

public class CommandDispatcher {
    private final Map<String, CommandExecutor> map = new HashMap<>();
    public CommandDispatcher(StoreEngine storeEngine){
        map.put(Command.GET.command, new GetExecutor(storeEngine));
        map.put(Command.SET.command, new SetExecutor(storeEngine));
    }
    public CommandExecutor getExecutor(String cmd) {
        CommandExecutor commandExecutor = map.get(cmd);
        if (commandExecutor == null) {
            throw new IllegalArgumentException("Unsupported command: " + cmd);
        }
        return commandExecutor;
    }
}
