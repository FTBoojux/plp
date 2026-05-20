package org.example.store.Command.executor.args;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


class CommandArgsParserTest {
    @Test
    public void parse(){
        List<String> args = List.of("");
        Assertions.assertThrows(IllegalArgumentException.class,() -> CommandArgsParser.parse(args));
    }
    @Test
    public void throw_exception_when_args_is_empty(){
        List<String> args = new ArrayList<>();
        Assertions.assertThrows(IllegalArgumentException.class, () -> CommandArgsParser.parse(args));
    }
    @Test
    public void parse_get_command() {
        List<String> args = List.of("GET","KEY");
        CommandArgs parse = CommandArgsParser.parse(args);
        Assertions.assertInstanceOf(GetCommandArgs.class, parse);
    }
    @Test
    public void throw_exception_when_only_2_args() {
        List<String> args = List.of("SET","KEY");
        Assertions.assertThrows(IllegalArgumentException.class, () -> CommandArgsParser.parse(args));
    }
    @Test
    public void parse_set_command() {
        List<String> args = List.of("SET","KEY","VALUE");
        SetCommandArgs command_parsed = (SetCommandArgs) CommandArgsParser.parse(args);
        Assertions.assertEquals("KEY",command_parsed.key());
        Assertions.assertEquals("VALUE",command_parsed.value());
    }
}