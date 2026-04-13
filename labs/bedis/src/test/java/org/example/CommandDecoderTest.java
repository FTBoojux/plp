package org.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.example.util.CommandParser;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommandDecoderTest {
    @Test
    public void completed_command_should_be_consumed_and_parsed() {
        EmbeddedChannel channel = createEmbeddedChannel();
        String parsedRESP = CommandParser.parseRESP("SET", "key", "val");
        ByteBuf input = Unpooled.wrappedBuffer(parsedRESP.getBytes());
        channel.writeInbound(input);

        String o = channel.readInbound();
        Assertions.assertNotNull(o);
        Assertions.assertEquals(parsedRESP,o);
    }

    private static @NonNull EmbeddedChannel createEmbeddedChannel() {
        CommandDecoder commandDecoder = new CommandDecoder();
        return new EmbeddedChannel(commandDecoder);
    }

    @Test
    public void partial_command_completes_when_remaining_bytes_arrive() {
        EmbeddedChannel channel = createEmbeddedChannel();
        String completedRespCommand = CommandParser.parseRESP("SET", "key", "val");
        String incompletedRespCommand = completedRespCommand.substring(0, completedRespCommand.length() - 1);
        String remainingRespCommand = completedRespCommand.substring(completedRespCommand.length() - 1);

        ByteBuf input = Unpooled.wrappedBuffer(incompletedRespCommand.getBytes());
        channel.writeInbound(input);

        Object o = channel.readInbound();
        Assertions.assertNull(o);

        ByteBuf remainingInput = Unpooled.wrappedBuffer(remainingRespCommand.getBytes());
        channel.writeInbound(remainingInput);

        Object completedOutput = channel.readInbound();
        Assertions.assertNotNull(completedOutput);
        Assertions.assertEquals(completedRespCommand, completedOutput);
    }
}
