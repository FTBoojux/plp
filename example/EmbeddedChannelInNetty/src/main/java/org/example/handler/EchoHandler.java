package org.example.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class EchoHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        StringBuilder sb = new StringBuilder();
        while (byteBuf.isReadable()) {
            sb.append((char)byteBuf.readByte());
        }
        ctx.write(sb.reverse().toString());
        ctx.flush();
    }
}
