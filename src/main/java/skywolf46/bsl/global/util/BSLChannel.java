package skywolf46.bsl.global.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;
import skywolf46.bsl.global.api.BSLCoreAPI;


public class BSLChannel {
    private Channel chan;

    public BSLChannel(Channel chan) {
        this.chan = chan;
    }

    public BSLChannel send(AbstractPacket pc) {
        if (!BSLCoreAPI.getSide().isCurentSide(pc.getSide())) {
            throw new IllegalStateException("Packet type " + pc.getClass().getSimpleName() + " not support sending in side " + BSLCoreAPI.getSide());
        }
        ByteBuf buf = Unpooled.directBuffer();
        buf.writeInt(pc.getID());
        BSLCoreAPI.getPacket(pc.getID()).writer().write(pc, buf);
        chan.writeAndFlush(buf);
        return this;
    }

    public BSLChannel sendSync(AbstractPacket pc) {
        if (!BSLCoreAPI.getSide().isCurentSide(pc.getSide())) {
            throw new IllegalStateException("Packet type " + pc.getClass().getSimpleName() + " not support sending in side " + BSLCoreAPI.getSide());
        }
        ByteBuf buf = Unpooled.directBuffer();
        buf.writeInt(pc.getID());
        BSLCoreAPI.getPacket(pc.getID()).writer().write(pc, buf);
        try {
            chan.writeAndFlush(buf).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }
}
