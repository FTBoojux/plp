package org.example.store.Command.executor;

import org.example.store.Command.executor.args.CommandArgs;

import java.util.List;

public sealed interface CommandExecutor <T extends CommandArgs>permits GetExecutor, SetExecutor {
    public String execute(T commandArgs);
}
