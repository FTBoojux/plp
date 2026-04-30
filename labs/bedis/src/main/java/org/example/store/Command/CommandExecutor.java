package org.example.store.Command;

import java.util.List;

public interface CommandExecutor {
    public String execute(List<String> command);
}
