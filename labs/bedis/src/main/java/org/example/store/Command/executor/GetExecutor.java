package org.example.store.Command.executor;

import org.example.store.Command.executor.args.CommandArgs;
import org.example.store.Command.executor.args.GetCommandArgs;
import org.example.store.engine.StoreEngine;

import java.util.List;
import java.util.Optional;

public final class GetExecutor implements CommandExecutor<GetCommandArgs> {
    final StoreEngine storeEngine;
    @Override
    public String execute(GetCommandArgs commandArgs) {
        String key = commandArgs.key();
        Optional<byte[]> value = storeEngine.get(key);
        return value.map(String::new).orElse("");
    }
    public GetExecutor(StoreEngine storeEngine) {
        this.storeEngine = storeEngine;
    }
}
