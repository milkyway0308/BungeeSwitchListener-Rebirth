package skywolf46.bsl.server.netty.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import skywolf46.bsl.global.BungeeSwitchListenerCore;
import skywolf46.bsl.global.api.BSLCoreAPI;
import skywolf46.bsl.global.util.BSLChannel;

import java.io.IOException;

public class BCDisconnectionHandler extends ChannelDuplexHandler {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (cause instanceof IOException) {
            BSLChannel chan = BungeeSwitchListenerCore.getChannel(ctx.channel());
            if (chan == null) {
                BSLCoreAPI.writer().printError("Unknown channel (" + ctx.channel().remoteAddress() + ") disconnected from bungeecord.");
            } else {
                BSLCoreAPI.writer().printError("Registered channel (Port " + chan.getPort() + ") disconnected from bungeecord.");
                BungeeSwitchListenerCore.unregisterChannel(chan.getPort());
            }
        }
    }
}
