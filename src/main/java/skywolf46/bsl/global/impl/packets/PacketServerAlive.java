package skywolf46.bsl.global.impl.packets;

import io.netty.buffer.ByteBuf;
import skywolf46.bsl.global.BungeeVariables;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;

public class PacketServerAlive extends AbstractPacket {
    private static PacketWriter writer = (packet, buffer) -> {

    };

    private static PacketReader reader = buf -> new PacketServerAlive();

    @Override
    public int getID() {
        return BungeeVariables.PACKET_KEEP_ALIVE;
    }

    @Override
    public PacketWriter writer() {
        return writer;
    }

    @Override
    public PacketReader reader() {
        return reader;
    }
}
