package org.example.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommandParserTest {

    public static final String STRING = "string";

    @Test
    public void convert_string_to_byte_buf() {
        ByteBuf buf = CommandParser.respBytes(STRING);
        ByteBuf target = Unpooled.wrappedBuffer(STRING.getBytes());
        Assertions.assertArrayEquals(target.array(), buf.array());
    }
    @Test
    public void convert_batch_string_args() {
        String parsedString = CommandParser.parseRESP("SET","KEY","VAL");
        Assertions.assertEquals(
            "*3\\r\\n$3\\r\\nSET\\r\\n$3\\r\\nKEY\\r\\n$3\\r\\nVAL\\r\\n",
                parsedString
        );
        String anotherString = CommandParser.parseRESP("SET","KEY","VAL","100");
        Assertions.assertEquals(
                "*4\\r\\n$3\\r\\nSET\\r\\n$3\\r\\nKEY\\r\\n$3\\r\\nVAL\\r\\n$3\\r\\n100\\r\\n",
                anotherString
        );
    }
}
