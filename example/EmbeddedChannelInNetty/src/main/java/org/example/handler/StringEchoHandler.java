package org.example.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.example.POJO.TlvMessage;

public class StringEchoHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        TlvMessage<String> message = (TlvMessage<String>)msg;
        ctx.write(message.value());
        ctx.flush();
    }
}
