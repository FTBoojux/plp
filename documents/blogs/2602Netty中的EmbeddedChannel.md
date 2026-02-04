# Netty 开发：使用 EmbeddedChannel 进行单元测试

> 💡 **本文源码说明**
>
> 本文所有演示代码（包含 `EchoHandler` 及完整的单元测试用例）均已开源。
> *   **代码仓库**：[https://github.com/FTBoojux/plp]
> *   **本章模块**：`example/EmbeddedChannelInNetty`
>
> 建议您将代码拉取到本地，直接运行 `EchoHandlerTest` 中的测试用例，体验更加直观。

## 1.前言：为什么需要EmbeddedChannel

### 1.1 痛点分析
在使用 Netty 进行网络编程时，编写和测试自定义的 ChannelHandler 是常见的需求。然而，直接在真实的网络环境中测试这些处理器存在诸多不便与挑战：
- 需要配置和构建完整的运行环境，增加了测试的复杂性和时间成本
- 难以模拟各种边缘情况和异常场景（如半包、粘包等）

### 1.2 解决方案
为了解决上述问题，Netty 提供了 `EmbeddedChannel` 类，它允许开发者在内存中模拟一个完整的 Channel 环境， 从而方便地对 ChannelHandler 进行单元测试。
通过 `EmbeddedChannel`，我们可以轻松地模拟发送和接收消息，验证处理器的行为，而无需依赖真实的网络连接。

使用 `EmbeddedChannel` 的优势有如下几点：
- 速度快。由于无需启动完整的运行环境，测试的运行速度能大幅提升
- 便于管理。就像单元测试一样，可以根据需要创建不同的测试类和测试方法
- 易于模拟各种场景。可以轻松地模拟各种边缘情况和异常场景

## 2. EmbeddedChannel 的基本使用

### 2.1 Netty 中的数据流
在 Netty 中，数据流动的过程可以简单地描述为：
1. 数据从网络中读取，进入 Channel 的输入端
2. 数据经过一系列的 ChannelHandler 进行处理
3. 处理后的数据通过 Channel 的输出端发送到网络

### 2.2 EmbeddedChannel 的工作原理
`EmbeddedChannel` 模拟了上述数据流动的过程。它允许我们在创建一个 Channel，并向其中添加自定义的 ChannelHandler。
`EmbeddedChannel` 提供了以下方法，来模拟数据的读写操作：
- `writeInbound(Object msg)`：模拟向 Channel 的输入端写入数据
- `writeOutbound(Object msg)`：模拟向 Channel 的输出端写入数据
- `readInbound()`：从 Channel 的输入端读取处理后的数据
- `readOutbound()`：从 Channel 的输出端读取处理后的数据

## 3. 示例代码

在本环节，我们通过开发一个将输入数据反转的简单 ChannelHandler，介绍如何使用 `EmbeddedChannel` 进行单元测试。

### 3.1 依赖

在项目的 `pom.xml` 文件中添加 `Netty` 和 `JUnit` 依赖：

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

### 3.2 示例1：实现一个反转字符串的 ChannelHandler

#### 3.2.1 测试先行

基于测试驱动开发（TDD）的理念，我们先编写测试代码：

```java
public class ReverseHandlerTest {
    @Test
    public void shouldReceiveData() {
        EmbeddedChannel channel = new EmbeddedChannel(new ReverseHandler());
        // 通过writeInbound方法模拟客户端发送数据到服务端
        channel.writeInbound(Unpooled.wrappedBuffer("hello world".getBytes()));
        // 通过readOutbound方法读取服务端处理后的数据
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

运行该测试用例，由于我们还没有实现 `ReverseHandler`，测试会失败。

#### 3.2.2 实现 ChannelHandler
接下来，我们实现 `ReverseHandler`：

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

现在，重新运行测试用例，所有测试都应该通过。

### 3.3 示例2：pipeline链式调用

通过 `EmbeddedChannel`的 `pipeline()` 方法，我们可以方便的测试多个handler协同工作的场景

首先我们准备两个测试用的handler `FirstHandler` 和 `SecondHandler`,他们会分别写回`1`和`2`到outbound中

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

接下来我们编写测试代码：

```java
public class PipelineTest {
    @Test
    public void testPipelineHandlers() {
        EmbeddedChannel channel = new EmbeddedChannel();
        // 添加handler到pipeline中
        channel.pipeline().addLast(new FirstHandler());
        channel.pipeline().addLast(new SecondHandler());

        // 模拟数据写入
        channel.writeInbound(Unpooled.wrappedBuffer("test".getBytes()));

        // 读取outbound数据
        Object msg1 = channel.readOutbound();
        Object msg2 = channel.readOutbound();

        // 验证结果
        Assertions.assertEquals("1", msg1);
        Assertions.assertEquals("2", msg2);
    }
}
```

运行该测试，我们发现测试不通过，断言信息如下：
```text
org.opentest4j.AssertionFailedError: 
Expected :2
Actual   :null
```

这说明我们的 `FirstHandler` 已经成功写回了 `1`，但是 `SecondHandler` 并没有写回 `2`。
这是因为在 Netty 的数据流动过程中，数据是从上到下流动的，而 `FirstHandler` 在处理完数据后并没有将数据传递给下一个 handler，导致 `SecondHandler` 没有机会处理数据。
我们可以在 `SecondHandler` 的第九行 `ctx.write("2");` 添加断点调试然后执行Debug运行，可以看到程序并没有进入 `SecondHandler` 的 `channelRead` 方法。

我们知道，在 Netty 中，如果一个 handler 处理完数据后没有调用 `ctx.fireChannelRead(msg);` 方法，数据就不会传递给下一个 handler。
因此，我们在 `FirstHandler` 的 `channelRead` 方法中添加 `ctx.fireChannelRead(msg);`:

```java
public class FirstHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.write("1");
        ctx.flush();
        // 传递数据给下一个handler
        ctx.fireChannelRead(msg);
    }
}
```

现在重新运行我们的测试用例，测试可以通过，Debug调试也可以正确进入 `SecondHandler` 的 `channelRead` 方法。

这说明 `EmbeddedChannel` 可以很好地模拟 Netty 的数据流动过程，帮助我们验证多个 handler 协同工作的场景。

### 3.4 补充说明

在 `3.2` 和 `3.3` 的示例中，我们主要使用到了 `writeInbound` 和 `readOutbound` 方法来模拟数据的写入和读取。
此外，我们也可以用 `writeOutbound` 来模拟向 Channel 的输出端写入数据，使用 `readInbound` 来读取输入端的数据。

```java
class EchoHandlerTest {
    @Test
    public void writeInboundManyTimes() {
        EmbeddedChannel channel = new EmbeddedChannel(new EchoHandler());
        channel.writeInbound(packString("1"));
        channel.writeInbound(packString("2"));
        channel.writeInbound(packString("3"));
        // 使用readOutbound读取处理后的数据
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
        // 使用readInbound读取输入端的数据
        Assertions.assertEquals(packString("1"), channel.readInbound());
        Assertions.assertEquals(packString("2"), channel.readInbound());
        Assertions.assertEquals(packString("3"), channel.readInbound());
    }
    public ByteBuf packString(String string) {
        return Unpooled.wrappedBuffer(string.getBytes());
    }
}
```

## 4. 总结

通过本文的介绍，我们了解了 Netty 中的 `EmbeddedChannel`类的基本使用方法，以及如何利用它进行 ChannelHandler 的单元测试。
他提供了一个简便的方式来模拟 Netty 的数据流动过程，使得我们可以在内存中轻松地测试和验证自定义的 ChannelHandler 的行为。