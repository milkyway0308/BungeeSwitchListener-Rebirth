package skywolf46.bsl.client.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;
import skywolf46.bsl.global.api.BSLCoreAPI;

public class PacketRecvHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        int packetID = buf.readInt();
        AbstractPacket packet = BSLCoreAPI.getPacket(packetID);
        if (packet == null) {
            BSLCoreAPI.writer().printError("Packet read error : Unknown packet id " + packetID);
            return;
        }
        AbstractPacket.PacketReader pr = packet.reader();
        if (pr == null) {
            BSLCoreAPI.writer().printError("Packet read error : Packet type " + packet.getClass().getSimpleName() + " not supports read in side " + BSLCoreAPI.getSide());
            return;
        }
        AbstractPacket packetForged = pr.read(buf);
        packet.listen(ctx.channel(), packetForged);
    }


}
