package org.example.store.Command.executor;

import org.example.store.Command.CommandExecutor;
import org.example.store.engine.StoreEngine;

import java.util.List;
import java.util.Optional;

public class GetExecutor implements CommandExecutor {
    final StoreEngine storeEngine;
    @Override
    public String execute(List<String> command) {
        if (command.size() != 2) {
            throw new WrongNumberOfArguments(2, command.size());
        }
        String key = command.get(1);
        Optional<byte[]> value = storeEngine.get(key);
        return value.map(String::new).orElse("");
    }
    public GetExecutor(StoreEngine storeEngine) {
        this.storeEngine = storeEngine;
    }
}
