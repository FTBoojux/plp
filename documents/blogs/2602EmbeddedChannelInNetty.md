# Netty development: Using EmbeddedChannel for Testing ChannelHandlers

## 1. Introduction: Why Use EmbeddedChannel
### 1.1 Pain Points
When using Netty for network programming, writing and testing custom ChannelHandlers is a common requirement.
However, testing these handlers directly in a real network environment presents several inconveniences and challenges:
- Configuring and building a complete runtime environment increases the complexity and time cost of testing.
- It is difficult to simulate various edge cases and exceptional scenarios (such as half-packets, sticky packets. etc.).

### 1.2 Solution
To address the above issues, Netty provides the `EmbeddedChannel` class, which allows developers to simulate a complete Channel environments in memory,
making it convenient to unit test ChannelHandlers.
With `EmbeddedChannel`, we can easily simulate sending and receiving messages, verify the behavior of handlers without relying on real network connections.

The advantages of using `EmbeddedChannel` are as follows:
- Fast speed. Since there is no need to start a complete runtime environment, the speed of tests can be greatly improved.
- Easy to manage. Just like unit tests, we can create different test classes and test methods as needed.
- Easy to simulate various scenarios. Various edge cases and exceptional scenarios can be easily simulated.

## 2. Basic Usage of EmbeddedChannel

### 2.1 Data Flow in Netty
In Netty, the data flow process can be simply described as:
1.Data is read from the network and enters the input end of the Channel.
2.Data is processed through a series of ChannelHandlers.
3.The processed data is sent to the network through the output end of the Channel.

### 2.2 Working Principle of EmbeddedChannel
`EmbeddedChannel` simulates the above data flow process. It allows us to create a Channel and add custom ChannelHandlers to it.
`EmbeddedChannel` provides the following methods to simulate data read and write operations:
- `writeInbound(Object msg)`: Simulates writing data to the input end of the Channel
- `writeOutbound(Object msg)`: Simulates writing data to the output end of the Channel
- `readInbound()`: Reads processed data from the input end of the Channel
- `readOutbound()`: Reads processed data from the output end of the Channel

## 3. Example Code
In this section, we will introduce how to use `EmbeddedChannel` for unit testing by developing a simple ChannelHandler that reverses the input data.

### 3.1 Dependencies
Add `Netty` and `JUnit` dependencies to the project's `pom.xml` file:
```xml
    <dependencies>
        <!--  Netty  -->
        <dependency>
            <groupId>io.netty</groupId>
            <artifactId>netty-all</artifactId>
            <version>4.2.9.Final</version>
            <scope>compile</scope>
        </dependency>
        <!--  JUnit  -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>6.0.1</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

### 3.2 Example 1: Implementing a ChannelHandler to reverse input data
#### 3.2.1 Test First
Based on the concept of Test-Driven Development (TDD), we first write the test code:
```java
public class ReverseHandlerTest {
    @Test
    public void shouldReceiveData() {
        EmbeddedChannel channel = new EmbeddedChannel(new ReverseHandler());
        // use `writeInbound` to simulate sending data to the server
        channel.writeInbound(Unpooled.wrappedBuffer("hello world".getBytes()));
        // use `readOutbound` to read the processed data
        Object msg = channel.readOutbound();
        Assertions.assertNotNull(msg);
    }

    @Test
    public void shouldReverseData() {
        EmbeddedChannel channel = new EmbeddedChannel(new ReverseHandler());
        channel.writeInbound(Unpooled.wrappedBuffer("hello world".getBytes()));
        Object msg = channel.readOutbound();
        Assertions.assertEquals("dlrow olleh", msg);
    }
}
```

The tests failed because the `ReverseHandler` has not been implemented yet.

#### 3.2.2 Implement the ReverseHandler

Next, we implement the `ReverseHandler` to make the tests pass:
```java
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
```

Now the tests should pass successfully.

### 3.3 Example 2: Pipeline with Multiple Handlers

We can easily test the scenario of multiple handlers by the `pipeline()` method of `EmbeddedChannel`.

First we create two handlers: `FirstHandler` and `SecondHandler`, they will write '1' and '2' to the output respectively.
```java
public class FirstHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.write("1");
        ctx.flush();
    }
}
```
```java
public class SecondHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.write("2");
        ctx.flush();
    }
}
```

Now we write test code:
```java
public class PipelineTest {
    @Test
    public void testPipelineHandlers() {
        EmbeddedChannel channel = new EmbeddedChannel();
        // add handler to pipeline
        channel.pipeline().addLast(new FirstHandler());
        channel.pipeline().addLast(new SecondHandler());

        // simulate inbound data
        channel.writeInbound(Unpooled.wrappedBuffer("test".getBytes()));

        // read outbound messages
        Object msg1 = channel.readOutbound();
        Object msg2 = channel.readOutbound();

        // verify the messages
        Assertions.assertEquals("1", msg1);
        Assertions.assertEquals("2", msg2);
    }
}
```

execute the test, the test failed and assertion error as follows:
```text
org.opentest4j.AssertionFailedError: 
Expected :2
Actual   :null
```

This indicates that the `FirstHandler` successfully wrote '1' to the output, but the `SecondHandler` did not write '2' as expected.
The reason is that in Netty's data flow, data flows from top to bottom, `FirstHandler` did not pass the data to the next handler after processing, so `SecondHandler` did not get a chance to process the data.
We can add a breakpoint in `SecondHandler`'s ninth line `ctx.write("2");` and Debug the test to verify that the code does not reach this line.

We know that in Netty, if a handler doesn't call `ctx.fireChannelRead(msg)` after processing data, the data will not be passed to the next handler.
So we add `ctx.fireChannelRead(msg);` in `FirstHandler`:
```java
public class FirstHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.write("1");
        ctx.flush();
        // pass the message to the next handler
        ctx.fireChannelRead(msg);
    }
}
```

Now, the test passes successfully, and Debug running can reach `SecondHandler`'s `channelRead` method.

It indicates that `EmbeddedChannel` can simulate Netty's data flow process well, allowing us to easily test the scenario of multiple handlers.

### 3.4 Supplementary Notes

In '3.2' and '3.3' , we mainly used `writeInbound` and `readOutbound` to simulate data's inbound and outbound flow.
in addition, we can also use `writeOutbound` to simulate writing data to the output end of the Channel and `readInbound` to read data from the input end.

```java
class EchoHandlerTest {
    @Test
    public void writeInboundManyTimes() {
        EmbeddedChannel channel = new EmbeddedChannel(new EchoHandler());
        channel.writeInbound(packString("1"));
        channel.writeInbound(packString("2"));
        channel.writeInbound(packString("3"));
        // use readOutbound to read outbound data
        Assertions.assertEquals("1", channel.readOutbound());
        Assertions.assertEquals("2", channel.readOutbound());
        Assertions.assertEquals("3", channel.readOutbound());
    }
    @Test
    public void writeInboundManyTimesWithoutHandler() {
        EmbeddedChannel channel = new EmbeddedChannel();
        channel.writeInbound(packString("1"));
        channel.writeInbound(packString("2"));
        channel.writeInbound(packString("3"));
        // use readInbound to read inbound data
        Assertions.assertEquals(packString("1"), channel.readInbound());
        Assertions.assertEquals(packString("2"), channel.readInbound());
        Assertions.assertEquals(packString("3"), channel.readInbound());
    }
    public ByteBuf packString(String string) {
        return Unpooled.wrappedBuffer(string.getBytes());
    }
}
```

## 4. Conclusion
In this article, we introduced the basic usage of `EmbeddedChannel` in Netty, and demonstrated how to use it for unit testing ChannelHandlers.
`EmbeddedChannel` provides a convenient way to simulate Netty's data flow process, allowing us to easily test and verify the behavior of ChannelHandlers.