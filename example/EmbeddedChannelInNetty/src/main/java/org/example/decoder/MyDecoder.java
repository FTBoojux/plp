package org.example.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.example.POJO.TlvMessage;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //  检查可读字节是否 >= 5 (Type + Length)。如果不满足，返回 (等待更多数据)
        while(byteBuf.readableBytes() >= 5) {
            // 标记当前 readerIndex。
            int readIndex = byteBuf.readerIndex();
            // 读取type和length
            byte type = byteBuf.readByte();
            ByteBuf lengthBuf = byteBuf.readBytes(4);
            int length = lengthBuf.readInt();
            lengthBuf.release();
            // 检查可读字节是否 >= Length
            if(byteBuf.readableBytes() < length) {
                // 重置 readerIndex 到标记位置 (回滚读取指针)
                byteBuf.readerIndex(readIndex);
                return;
            } else {
                // 读取 Value (长度为 Length 的字节),并将解析的对象输出
                ByteBuf informationBuf = byteBuf.readBytes(length);
                String information = informationBuf.toString(StandardCharsets.UTF_8);
                TlvMessage<String> tlvMessage = new TlvMessage<>(type, length, information);
                informationBuf.release();
                list.add(tlvMessage);
            }
        }
    }
}
