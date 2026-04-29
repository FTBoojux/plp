package org.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.example.exceptions.NotEnoughDataException;
import org.example.exceptions.ProtocolViolationException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readerIndex = in.readerIndex();
        int readableBytes = in.readableBytes();
        String lineNumString = readLine(in);
        if (!lineNumString.startsWith("*")) {
            throw new ProtocolViolationException(String.format("Protocol violation: Expected line to start with '*', but got '%s'", lineNumString));
        }
        int lineNum = Integer.parseInt(lineNumString.substring(1));
        List<String> elements = new ArrayList<>();
        try{
            for(int i = 0; i < lineNum; ++i) {
                String line = readLine(in);
                if (Objects.isNull(line)) {
                    break;
                }
                if(!line.startsWith("$")){
                    throw new ProtocolViolationException(String.format("Protocol violation: Expected line to start with '$', but got '%s'", line));
                }
                String lenString = line.substring(1);
                int len = Integer.parseInt(lenString);
                if (in.readableBytes() < len + 2) {
                    throw new NotEnoughDataException();
                }
                byte[] bytes = new byte[len];
                in.readBytes(bytes);
                String data = new String(bytes, StandardCharsets.UTF_8);
                elements.add(data);
                // skip \r\n
                in.readBytes(2);
            }
            out.add(elements);
        } catch (NotEnoughDataException e) {
            in.resetReaderIndex();
        }
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
