package skywolf46.bsl.client.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import skywolf46.bsl.client.BukkitSwitchListener;
import skywolf46.bsl.client.netty.handler.BukkitInitializeHandler;
import skywolf46.bsl.global.BungeeSwitchListenerCore;
import skywolf46.bsl.global.api.BSLCoreAPI;
import skywolf46.bsl.global.util.BSLChannel;

import java.net.InetSocketAddress;

public class BukkitFakePlayerConnection {
    private Channel channel;
    private Bootstrap strap;

    public BukkitFakePlayerConnection() {
        EventLoopGroup group = new NioEventLoopGroup();
        strap = new Bootstrap();
        strap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new BukkitInitializeHandler());
        try {
            channel = strap.connect(new InetSocketAddress(BukkitSwitchListener.getConfiguration().getIP(), BukkitSwitchListener.getConfiguration().getPort())).sync().channel();
            BSLCoreAPI.writer().printText("Connected!");
            BungeeSwitchListenerCore.registerChannel(-1, new BSLChannel(channel, -1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void send(ByteBuf buf) {

    }
}
