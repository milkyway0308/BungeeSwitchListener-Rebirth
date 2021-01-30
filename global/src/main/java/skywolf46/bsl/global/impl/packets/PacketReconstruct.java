package skywolf46.bsl.global.impl.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import skywolf46.bsl.global.BungeeVariables;
import skywolf46.bsl.global.abstraction.enums.Side;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;

public class PacketReconstruct extends AbstractPacket {
    private ByteBuf buf;

    public PacketReconstruct(ByteBuf buf) {
        this.buf = Unpooled.directBuffer();
        this.buf.writeBytes(buf);
    }

    public PacketReconstruct() {

    }

    @Override
    public int getID() {
        return BungeeVariables.PACKET_RECONSTRUCT;
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    public ByteBuf getBuffer() {
        return buf;
    }

    private int index = 0;

    @Override
    public void retainBuffer() {
        getBuffer().retain();
    }

    @Override
    public void releaseBuffer() {
        getBuffer().release();
    }

    @Override
    public void markBuffer() {
        index = getBuffer().readerIndex();
    }

    @Override
    public void resetBuffer() {
        getBuffer().readerIndex(index);
    }
}
