package org.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class CommandDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readerIndex = in.readerIndex();
        int readableBytes = in.readableBytes();
        byte[] byteArray = new byte[readableBytes];
        in.readBytes(byteArray);
        String string = new String(byteArray);
        out.add(string);
        throw new UnsupportedOperationException("This function has not be impleted");
    }
}
