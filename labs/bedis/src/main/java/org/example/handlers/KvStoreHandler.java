package org.example.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.example.store.Command.CommandDispatcher;
import org.example.store.Command.executor.CommandExecutor;
import org.example.store.Command.executor.args.CommandArgs;
import org.example.store.Command.executor.args.CommandArgsParser;

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
        String command = commands.getFirst();
        CommandArgs args = CommandArgsParser.parse(commands);
        CommandExecutor executor = commandDispatcher.getExecutor(command);
        String response = executor.execute(args);
        ctx.write(new TextWebSocketFrame(response));
        ctx.flush();
    }
}
