package org.example.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PipelineTest {
    @Test
    public void testPipelineHandlers() {
        EmbeddedChannel channel = new EmbeddedChannel();
        // 添加handler到pipeline中
        channel.pipeline().addLast(new FirstHandler());
        channel.pipeline().addLast(new SecondHandler());

        // 模拟数据写入
        channel.writeInbound(Unpooled.wrappedBuffer("test".getBytes()));

        // 读取outbound数据
        Object msg1 = channel.readOutbound();
        Object msg2 = channel.readOutbound();

        // 验证结果
        Assertions.assertEquals("1", msg1);
        Assertions.assertEquals("2", msg2);
    }
}
