package org.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.example.util.CommandParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommandDecoderTest {
    @Test
    public void completed_command_should_be_consumed_and_parsed() {
        CommandDecoder commandDecoder = new CommandDecoder();
        EmbeddedChannel channel = new EmbeddedChannel(commandDecoder);
        String parsedRESP = CommandParser.parseRESP("SET", "key", "val");
        ByteBuf input = Unpooled.wrappedBuffer(parsedRESP.getBytes());
        channel.writeInbound(input);

        String o = channel.readInbound();
        Assertions.assertNotNull(o);
        Assertions.assertEquals(parsedRESP,o);
    }
}
