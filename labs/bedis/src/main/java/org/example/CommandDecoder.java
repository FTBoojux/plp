package org.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.example.exceptions.NotEnoughDataException;
import org.example.exceptions.ProtocolViolationException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CommandDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {
    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame webSocketFrame, List<Object> out) throws Exception {
        ByteBuf in = webSocketFrame.content();
        int readerIndex = in.readerIndex();
        int readableBytes = in.readableBytes();
        String lineNumString = readLine(in);
        String[] commandArray = lineNumString.split(" ");
        out.add(Arrays.asList(commandArray));
    }

    private String readLine(ByteBuf in) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (in.isReadable()) {
            byte b = in.readByte();
            if (b == '\r') {
                if (in.isReadable() && in.getByte(in.readerIndex()) == '\n') {
                    // consume \n
                    in.readByte();
                    break;
                }
            }
            baos.write(b);
        }
        return baos.toString(StandardCharsets.UTF_8);
    }
}
