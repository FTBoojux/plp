package org.example.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReverseHandlerTest {
    @Test
    public void shouldReceiveData() {
        EmbeddedChannel channel = new EmbeddedChannel(new ReverseHandler());
        // 通过writeInbound方法模拟客户端发送数据到服务端
        channel.writeInbound(Unpooled.wrappedBuffer("hello world".getBytes()));
        // 通过readOutbound方法读取服务端处理后的数据
        Object msg = channel.readOutbound();
        Assertions.assertNotNull(msg);
    }
    @Test
    public void shouldReverseData() {
        EmbeddedChannel channel = new EmbeddedChannel(new ReverseHandler());
        channel.writeInbound(Unpooled.wrappedBuffer("hello world".getBytes()));
        Object msg = channel.readOutbound();
        Assertions.assertEquals("dlrow olleh", msg);
    }

    public ByteBuf packString(String string) {
        return Unpooled.wrappedBuffer(string.getBytes());
    }

}
