package org.example.store.Command.executor;

import org.example.store.Command.executor.CommandExecutor;
import org.example.store.Command.executor.args.SetCommandArgs;
import org.example.store.engine.StoreEngine;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class SetExecutor implements CommandExecutor<SetCommandArgs> {
    final StoreEngine storeEngine;
    @Override
    public String execute(SetCommandArgs command) {
//        if (command.size() < 3) {
//            throw new IllegalArgumentException("SET command requires at least 3 arguments");
//        }
//        String key = command.get(1);
//        String value = command.get(2);
//        String expire = "";
//        if(command.size() == 4) {
//            expire = command.get(3);
//            int expireInt = Integer.parseInt(expire);
//            storeEngine.set(key, value.getBytes(StandardCharsets.UTF_8), expireInt, TimeUnit.SECONDS);
//        }else{
//            storeEngine.set(key, value.getBytes(StandardCharsets.UTF_8));
//        }
        return "OK";
    }
    public SetExecutor(StoreEngine storeEngine) {
        this.storeEngine = storeEngine;
    }
}
