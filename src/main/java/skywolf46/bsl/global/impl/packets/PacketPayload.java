package skywolf46.bsl.global.impl.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import skywolf46.bsl.global.BungeeVariables;
import skywolf46.bsl.global.abstraction.enums.Side;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;

public class PacketPayload extends AbstractPacket {
    private ByteBuf stream;

    public PacketPayload() {
        this(true);
    }

    public PacketPayload(boolean create) {
        if (create)
            this.stream = Unpooled.directBuffer();
    }

    public PacketPayload(ByteBuf buf) {
        this.stream = buf;
    }

    public ByteBuf getBuffer() {
        return stream;
    }

    @Override
    public int getID() {
        return BungeeVariables.PACKET_GLOBAL_PAYLOAD;
    }

    @Override
    public Side getSide() {
        return Side.GLOBAL;
    }
}
