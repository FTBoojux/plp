package org.example.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EchoHandlerTest {
    @Test
    public void echoShouldReturnMsg() {
        EmbeddedChannel channel = new EmbeddedChannel(new EchoHandler());
        channel.writeInbound(Unpooled.wrappedBuffer("ok!".getBytes()));
        Object msg = channel.readOutbound();
        Assertions.assertNotNull(msg);
    }
    @Test
    public void echoShouldReverseTheSameMsg() {
        EmbeddedChannel channel = new EmbeddedChannel(new EchoHandler());
        channel.writeInbound(Unpooled.wrappedBuffer("ok!".getBytes()));
        Object msg = channel.readOutbound();
        Assertions.assertEquals("!ko",msg);
    }
    @Test
    public void readInboundShouldEqualToWrite() {
        EmbeddedChannel channel = new EmbeddedChannel();
        String information = "439";
        channel.writeInbound(information);
        Object obj = channel.readInbound();
        Assertions.assertEquals(information, obj);
    }
    @Test
    public void writeInboundManyTimes() {
        EmbeddedChannel channel = new EmbeddedChannel(new EchoHandler());
        channel.writeInbound(packString("1"));
        channel.writeInbound(packString("2"));
        channel.writeInbound(packString("3"));
        Assertions.assertEquals("1", channel.readOutbound());
        Assertions.assertEquals("2", channel.readOutbound());
        Assertions.assertEquals("3", channel.readOutbound());
    }
    @Test
    public void writeInboundManyTimesWithoutHandler() {
        EmbeddedChannel channel = new EmbeddedChannel();
        channel.writeInbound(packString("1"));
        channel.writeInbound(packString("2"));
        channel.writeInbound(packString("3"));
        Assertions.assertEquals(packString("1"), channel.readInbound());
        Assertions.assertEquals(packString("2"), channel.readInbound());
        Assertions.assertEquals(packString("3"), channel.readInbound());
    }
    public ByteBuf packString(String string) {
        return Unpooled.wrappedBuffer(string.getBytes());
    }
}