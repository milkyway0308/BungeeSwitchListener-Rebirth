package skywolf46.bsl.client.netty.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import skywolf46.bsl.global.api.BSLCoreAPI;

import java.io.IOException;

public class BKDisconnectionHandler extends ChannelDuplexHandler {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof IOException) {
            BSLCoreAPI.writer().printError("Bungee disconnected! Waiting for connection...");
        }
    }
}
