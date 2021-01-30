package skywolf46.bsl.client.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.bukkit.Bukkit;
import skywolf46.bsl.client.BukkitSwitchListener;
import skywolf46.bsl.global.BungeeVariables;
import skywolf46.bsl.global.api.BSLCoreAPI;
import skywolf46.bsl.global.impl.packets.PacketValidation;

public class BukkitInitializeHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf bb = Unpooled.buffer();
        bb.writeInt(BungeeVariables.BUNGEE_IDENTIFIER_01);
        bb.writeInt(BungeeVariables.BUNGEE_IDENTIFIER_02);
        bb.writeInt(BungeeVariables.BUNGEE_IDENTIFIER_03);
        bb.writeInt(BungeeVariables.BUNGEE_IDENTIFIER_04);
        bb.writeInt(Bukkit.getPort());
        ctx.writeAndFlush(bb);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        if (buf.readInt() == BungeeVariables.BUNGEE_IDENTIFIER_01
                && buf.readInt() == BungeeVariables.BUNGEE_IDENTIFIER_02
                && buf.readInt() == BungeeVariables.BUNGEE_IDENTIFIER_03
                && buf.readInt() == BungeeVariables.BUNGEE_IDENTIFIER_04) {
            BSLCoreAPI.writer().printText("Bungee communication success! Changing handlers...");
            ctx.pipeline().remove(this);
            ctx.pipeline().addFirst("recv-listener", new PacketRecvHandler());
            ctx.pipeline().addFirst("buffer-encoder", new LengthFieldPrepender(8));
            ctx.pipeline().addFirst("buffer-decoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 8, 0, 8));
            ctx.pipeline().addLast("exception-listener", new BKDisconnectionHandler());
//            PacketPayload test = new PacketPayload(true);
//            ByteBufUtility.writeString(test.getBuffer(), "Hello from client port " + Bukkit.getPort() + "!");
//            test.getBuffer().writeInt(4401);
            BSLCoreAPI.bungee().send(new PacketValidation(BukkitSwitchListener.getConfiguration().getIdentify(), Bukkit.getPort()));
        }
    }
}