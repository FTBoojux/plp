package org.example.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.CharsetUtil;
import org.example.handler.StringEchoHandler;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MyDecoderTest {
    @Test
    public void return_when_unreadable(){
        ByteBuf buf = Unpooled.buffer(4);
        buf.writeInt(4);
        MyDecoder myDecoder = new MyDecoder();
        EmbeddedChannel channel = new EmbeddedChannel(
                myDecoder,
                new StringEchoHandler()
                );
        channel.writeInbound(buf);
    }
    @Test
    public void normal_package_should_echo(){
        ByteBuf hello = encodeFrame("hello");
        EmbeddedChannel channel = getEmbeddedChannel();
        channel.writeInbound(Unpooled.wrappedBuffer(hello));
        Object out = channel.readOutbound();
        Assertions.assertEquals("hello",out);
    }

    @Test
    public void sticky_packets_should_echo() {
        ByteBuf hello = encodeFrame("hello");
        ByteBuf world = encodeFrame("world");
        ByteBuf sticky_packets = Unpooled.wrappedBuffer(hello, world);
        EmbeddedChannel channel = getEmbeddedChannel();
        channel.writeInbound(sticky_packets);

        Object helloResponse = channel.readOutbound();
        Assertions.assertEquals("hello", helloResponse, "应当返回'hello'");

        Object worldResponse = channel.readOutbound();
        Assertions.assertEquals("world", worldResponse, "应当返回'world'");

        Assertions.assertNull(channel.readOutbound());
    }

    @Test
    public void split_header_should_echo() {
        ByteBuf hello = encodeFrame("hello");
        EmbeddedChannel embeddedChannel = getEmbeddedChannel();
        embeddedChannel.writeInbound(hello.copy(0, 5));
        embeddedChannel.writeInbound(hello.copy(5, hello.readableBytes() - 5));
        Object response = embeddedChannel.readOutbound();
        Assertions.assertEquals("hello", response, "应当返回'hello'");
    }

    @Test
    public void split_body_should_echo() {
        ByteBuf hello = encodeFrame("hello");
        EmbeddedChannel embeddedChannel = getEmbeddedChannel();
        embeddedChannel.writeInbound(hello.copy(0, 7));
        embeddedChannel.writeInbound(hello.copy(7, hello.readableBytes() - 7));
        Object response = embeddedChannel.readOutbound();
        Assertions.assertEquals("hello", response, "应当返回'hello'");
    }

    @Test
    public void complex_case_should_echo_separately() {
        ByteBuf hello = encodeFrame("hello");
        ByteBuf world = encodeFrame("world");
        EmbeddedChannel embeddedChannel = getEmbeddedChannel();

        embeddedChannel.writeInbound(hello.copy(0, 4));
        Object response = embeddedChannel.readOutbound();
        Assertions.assertNull(response);

        embeddedChannel.writeInbound(Unpooled.wrappedBuffer(hello.copy(4, hello.readableBytes() - 4), world.copy(0, 5)));
        Object helloResponse = embeddedChannel.readOutbound();
        Assertions.assertEquals("hello", helloResponse, "应当返回'hello'");
        Assertions.assertNull(embeddedChannel.readOutbound());

        embeddedChannel.writeInbound(world.copy(5, world.readableBytes() - 5));
        Object worldResponse = embeddedChannel.readOutbound();
        Assertions.assertEquals("world", worldResponse, "应当返回'world'");
    }

    private static @NonNull EmbeddedChannel getEmbeddedChannel() {
        return new EmbeddedChannel(
                new MyDecoder(),
                new StringEchoHandler()
        );
    }


    private static ByteBuf encodeFrame(String s) {
        byte[] bytes = s.getBytes(CharsetUtil.UTF_8);
        ByteBuf buf = Unpooled.buffer(5 + bytes.length);
        // 本实验中1表示字符串类型
        buf.writeByte(1);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        return buf;
    }
}
