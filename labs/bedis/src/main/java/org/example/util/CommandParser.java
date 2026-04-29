package org.example.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Objects;

public class CommandParser {
    public static final String BOUNDARY = "\r\n";
    public static ByteBuf respBytes(String string) {
        return Unpooled.wrappedBuffer(Objects.requireNonNullElse(string, "").getBytes());
    }

    public static String parseRESP(String... args) {
        StringBuilder builder = new StringBuilder();
        builder.append('*');
        builder.append(args.length);
        builder.append(BOUNDARY);
        for (String arg : args) {
            builder.append('$');
            builder.append(arg.length());
            builder.append(BOUNDARY);
            builder.append(arg);
            builder.append(BOUNDARY);
        }
        return builder.toString();
    }
}
