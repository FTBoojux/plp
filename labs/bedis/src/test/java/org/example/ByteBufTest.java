package org.example;

import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.example.handlers.KvStoreHandler;
import org.example.store.Command.CommandDispatcher;
import org.example.store.engine.SimpleStoreEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ByteBufTest {
    @Test
    public void whatDoesCapacityReturn() {
        EmbeddedChannel channel
                = new EmbeddedChannel(new KvStoreHandler(new CommandDispatcher(new SimpleStoreEngine())));
        String input = "set";
        channel.writeInbound(Unpooled.wrappedBuffer(input.getBytes()));
        Object res = channel.readOutbound();
        Assertions.assertEquals(input.length(),res);
    }
}
