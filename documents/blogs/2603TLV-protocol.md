# Netty Development Advanced: Testing Half-Packet and Sticky-Packet Scenarios with EmbeddedChannel

## 1. Introduction: What is the TLV Protocol?

In this section, we will further demonstrate the use of Netty's `EmbeddedChannel` by implementing a custom TLV (Type-Length-Value) protocol decoder.

### 1.1 Pain Points: Half-Packets and Sticky-Packets

Half-packets and sticky-packets are common issues when transmitting data over a network.

- Half-packet: A single data packet is split into multiple packets during transmission. This can be caused by reasons such as the original data packet being too large, writing speed being too fast, or the TCP window size being too small.
- Sticky-packet: Multiple data packets are coalesced into a single packet during transmission. This can be caused by reasons such as the original data packets being too small, writing speed being too slow, or the TCP window size being too large.

The root cause of these issues is that TCP is a stream-oriented protocol, which does not guarantee the integrity of application-layer data packets (it maintains stream integrity, not packet boundaries).

### 1.2 Solution: The TLV Protocol

The Type-Length-Value (TLV) protocol is a common solution to address half-packet and sticky-packet scenarios. It organizes data into three parts:
- Type/Tag: An identifier used to distinguish the type of data.
- Length: A number that specifies the length of the data, which is typically determined by the payload size.
- Value: The actual data being transmitted.

## 2. Practical Implementation

### 2.1 Data Structure

First, we define our TLV message structure as follows:
- Type: Represents the data type, 1 byte
- Length: Represents the data length, 4 bytes
- Value: The actual data, with length determined by the Length field
```java
public record TlvMessage<T>(byte type,int length,T value) {
}
```

### 2.2 Define Custom TLV Decoder

We define the following TLV decoding logic:
- 1. Check if the current readable bytes are >= 5 (type + length). If not, return and wait for more data.
- 2. Mark the current `readerIndex`.
- 3. Read type.
- 4. Read length.
- 5. Check if the remaining readable bytes are >= length.
- 6. If not:
  - Reset `readerIndex` and return to wait for more data.
- 7. If satisfied:
  - Read the number of bytes specified by length as value.

The implementation for this experiment is as follows:
```java
public class MyDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //  Check if the readable bytes are >= 5 (Type + Length). If not, return (wait for more data)
        while(byteBuf.readableBytes() >= 5) {
            // Mark the current readerIndex.
            int readIndex = byteBuf.readerIndex();
            // Read type and length
            byte type = byteBuf.readByte();
            int length = byteBuf.readInt();
            // Check if the remaining readable bytes are > length
            if(byteBuf.readableBytes() < length) { 
                // If not, roll back readerIndex and return (wait for more data)
                byteBuf.readerIndex(readIndex);
                return;
            } else {
              // Read value and construct TlvMessage
                ByteBuf informationBuf = byteBuf.readBytes(length);
                String information = informationBuf.toString(StandardCharsets.UTF_8);
                TlvMessage<String> tlvMessage = new TlvMessage<>(type, length, information);
                informationBuf.release();
                list.add(tlvMessage);
            }
        }
    }
}
```

Meanwhile we use a simple `StringEchoHandler` to return data.
```java
public class StringEchoHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        TlvMessage<String> message = (TlvMessage<String>)msg;
        ctx.write(message.value());
        ctx.flush();
    }
}
```

### 2.3 Utility Method
We define utility methods to create `EmbeddedChannel` and data packets for testing purposes:
```java
public class MyDecoderTest {
    private static @NonNull EmbeddedChannel getEmbeddedChannel() {
        return new EmbeddedChannel(
                new MyDecoder(),
                new StringEchoHandler()
        );
    }
    private static ByteBuf encodeFrame(String s) {
        byte[] bytes = s.getBytes(CharsetUtil.UTF_8);
        ByteBuf buf = Unpooled.buffer(5 + bytes.length);
        // In this experiment, 1 represents the String type
        buf.writeByte(1);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        return buf;
    }
}
```

### 2.4 Test cases

#### 2.4.1 Normal Case
```java
public class MyDecoderTest {
    @Test
    public void normal_package_should_echo(){
        ByteBuf hello = encodeFrame("hello");
        EmbeddedChannel channel = getEmbeddedChannel();
        channel.writeInbound(Unpooled.wrappedBuffer(hello));
        Object out = channel.readOutbound();
        Assertions.assertEquals("hello",out);
    }
}
```

#### 2.4.2 Sticky-Packet
We combine two data packets into a single buffer and send it to verify that our `MyDecoder` can split them correctly.
```java
public class MyDecoderTest {
    @Test
    public void sticky_packets_should_echo() {
        ByteBuf hello = encodeFrame("hello");
        ByteBuf world = encodeFrame("world");
        ByteBuf sticky_packets = Unpooled.wrappedBuffer(hello, world);
        EmbeddedChannel channel = getEmbeddedChannel();
        channel.writeInbound(sticky_packets);
    
        Object helloResponse = channel.readOutbound();
        Assertions.assertEquals("hello", helloResponse, "should return 'hello'");
    
        Object worldResponse = channel.readOutbound();
        Assertions.assertEquals("world", worldResponse, "should return 'world'");
    
        Assertions.assertNull(channel.readOutbound());
    }
}
```

#### 2.4.3 Half-Packet (Split Header)

We split a data packet into two parts at the boundary between the length and value fields, to verify that our `MyDecoder` can correctly wait for the complete data.
```java
public class MyDecoderTest {
    @Test
    public void split_header_should_echo() {
        ByteBuf hello = encodeFrame("hello");
        EmbeddedChannel embeddedChannel = getEmbeddedChannel();
        embeddedChannel.writeInbound(hello.copy(0, 5));
        embeddedChannel.writeInbound(hello.copy(5, hello.readableBytes() - 5));
        Object response = embeddedChannel.readOutbound();
        Assertions.assertEquals("hello", response, "should return 'hello'");
    }
}
```

#### 2.4.4 Half-Packet (Split Value)

We split a data packet at the middle of the value part, to verify that our `MyDecoder` can correctly wait for the complete data when the header is complete but the value is not.
```java
public class MyDecoderTest {
    @Test
    public void split_body_should_echo() {
        ByteBuf hello = encodeFrame("hello");
        EmbeddedChannel embeddedChannel = getEmbeddedChannel();
        embeddedChannel.writeInbound(hello.copy(0, 7));
        embeddedChannel.writeInbound(hello.copy(7, hello.readableBytes() - 7));
        Object response = embeddedChannel.readOutbound();
        Assertions.assertEquals("hello", response, "should return 'hello'");
    }
}
```

#### 2.4.5 Complex Mixed Case
We prepare two data packets and send them in the following sequence:

- 1. The first half of the 'hello' packet
- Read: there should be no response
- 2. The second half of the 'hello' packet + the first half of the 'world' packet
- Read: there should be a response 'hello'
- 3. The second half of the 'world' packet
- Read: there should be a response 'world'
```java
public class MyDecoderTest {
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
        Assertions.assertEquals("hello", helloResponse, "should return 'hello'");
        Assertions.assertNull(embeddedChannel.readOutbound());

        embeddedChannel.writeInbound(world.copy(5, world.readableBytes() - 5));
        Object worldResponse = embeddedChannel.readOutbound();
        Assertions.assertEquals("world", worldResponse, "should return 'world'");
    }
}
```

## 3. Conclusion
Through this experiment, we have successfully implemented a custom TLV protocol decoder using Netty's `EmbeddedChannel` and verified its correctness in handling half-packet and sticky-packet scenarios.