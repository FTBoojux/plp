package org.example.store.engine.Command;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.example.CommandDecoder;
import org.example.handlers.KvStoreHandler;
import org.example.store.Command.CommandDispatcher;
import org.example.store.engine.SimpleStoreEngine;
import org.example.store.engine.StoreEngine;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class CommandTest {
    @Test
    public void setShouldSucceed() {
        EmbeddedChannel channel = getEmbeddedChannel();
        String setCommand = "SET KEY VAL";
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(setCommand);
        channel.writeInbound(textWebSocketFrame);
        TextWebSocketFrame res  = channel.readOutbound();
        Assertions.assertEquals("OK", res.text());
    }

    private static @NonNull EmbeddedChannel getEmbeddedChannel() {
        EmbeddedChannel channel = new EmbeddedChannel();
        SimpleStoreEngine storeEngine = new SimpleStoreEngine();
        CommandDispatcher commandDispatcher = new CommandDispatcher(storeEngine);
        channel.pipeline()
                .addLast(new CommandDecoder())
                .addLast(new KvStoreHandler(commandDispatcher))
        ;
        return channel;
    }

    @Test
    public void shouldGetValAfterSetHaveSucceed() {
        EmbeddedChannel channel = getEmbeddedChannel();
        String setCommand = "SET KEY VAL";
        TextWebSocketFrame setCmdSocketFrame = new TextWebSocketFrame(setCommand);
        channel.writeInbound(setCmdSocketFrame);
        channel.readOutbound();

        String getCommand = "GET KEY";
        TextWebSocketFrame getCmdSocketFrame = new TextWebSocketFrame(getCommand);
        channel.writeInbound(getCmdSocketFrame);
        TextWebSocketFrame valOfKey  = channel.readOutbound();
        Assertions.assertEquals("VAL", valOfKey.text());
    }
    @Test
    public void setValWithExpireTIme() {
        EmbeddedChannel channel = new EmbeddedChannel();
        SimpleStoreEngine storeEngine = new SimpleStoreEngine();
        CommandDispatcher commandDispatcher = new CommandDispatcher(storeEngine);
        channel.pipeline()
                .addLast(new CommandDecoder())
                .addLast(new KvStoreHandler(commandDispatcher))
        ;
        String setCommand = "SET KEY VAL 60";
        TextWebSocketFrame setCmdSocketFrame = new TextWebSocketFrame(setCommand);
        channel.writeInbound(setCmdSocketFrame);
        channel.readOutbound();

        String getCommand = "GET KEY";
        TextWebSocketFrame getCmdSocketFrame = new TextWebSocketFrame(getCommand);
        channel.writeInbound(getCmdSocketFrame);
        TextWebSocketFrame valOfKey  = channel.readOutbound();
        Assertions.assertEquals("VAL", valOfKey.text());
    }
    @Test
    public void getNullAfterKeyHasExpired() {
        EmbeddedChannel channel = new EmbeddedChannel();
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        SimpleStoreEngine storeEngine = new SimpleStoreEngine();
        storeEngine.setClock(clock);
        CommandDispatcher commandDispatcher = new CommandDispatcher(storeEngine);
        channel.pipeline()
                .addLast(new CommandDecoder())
                .addLast(new KvStoreHandler(commandDispatcher))
        ;
        String setCommand = "SET KEY VAL 60";
        TextWebSocketFrame setCmdSocketFrame = new TextWebSocketFrame(setCommand);


        channel.writeInbound(setCmdSocketFrame);
        channel.readOutbound();

        String getCommand = "GET KEY";
        TextWebSocketFrame getCmdSocketFrame = new TextWebSocketFrame(getCommand);
        clock = Clock.offset(clock, Duration.of(61, ChronoUnit.SECONDS));
        storeEngine.setClock(clock);
        channel.writeInbound(getCmdSocketFrame);
        TextWebSocketFrame valOfKey  = channel.readOutbound();
        Assertions.assertEquals("", valOfKey.text());
    }
    @Test
    public void throwIllegalArgumentExceptionWhenExpireTimeIsNotNumber() {
        EmbeddedChannel embeddedChannel = getEmbeddedChannel();
        String setCommand = "SET KEY VAL NOT_INTEGER";
        TextWebSocketFrame setCmdSocketFrame = new TextWebSocketFrame(setCommand);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            embeddedChannel.writeInbound(setCmdSocketFrame);
            TextWebSocketFrame valOfKey = embeddedChannel.readOutbound();
        });
    }
}
