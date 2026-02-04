package org.example.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ReverseHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        StringBuilder sb = new StringBuilder();
        while (byteBuf.isReadable()) {
            sb.append((char) byteBuf.readByte());
        }
        ctx.write(sb.reverse().toString());
        ctx.flush();
    }
}
