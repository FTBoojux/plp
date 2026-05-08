package org.example.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.example.store.Command.CommandDispatcher;
import org.example.store.Command.CommandExecutor;

import java.util.Arrays;
import java.util.List;

public class KvStoreHandler extends ChannelInboundHandlerAdapter {
    private final CommandDispatcher commandDispatcher;
    public KvStoreHandler(CommandDispatcher commandDispatcher){
        this.commandDispatcher = commandDispatcher;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        List<String> commands = (List<String>) msg;
        String command = commands.get(0);
        CommandExecutor executor = commandDispatcher.getExecutor(command);
        String response = executor.execute(commands);
        ctx.write(new TextWebSocketFrame(response));
        ctx.flush();
    }
}
