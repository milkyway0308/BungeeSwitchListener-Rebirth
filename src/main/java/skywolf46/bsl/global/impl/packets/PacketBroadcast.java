package skywolf46.bsl.global.impl.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import skywolf46.bsl.global.BungeeVariables;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;
import skywolf46.bsl.global.api.BSLCoreAPI;

public class PacketBroadcast extends AbstractPacket {
    private int port;
    private ByteBuf buffer;

    public PacketBroadcast(int port, AbstractPacket packet) {
        buffer = Unpooled.directBuffer();
        buffer.writeInt(this.port = port);
        buffer.writeInt(packet.getID());
        BSLCoreAPI.getPacket(packet.getID()).writer().write(packet, buffer);
    }

    public PacketBroadcast(ByteBuf buf) {
        buf.markReaderIndex();
        this.port = buf.readInt();
        this.buffer = buf;
        buf.resetReaderIndex();
    }

    public PacketBroadcast() {

    }

    public ByteBuf getBuffer() {
        return buffer;
    }


    public int getPort() {
        return port;
    }

    @Override
    public int getID() {
        return BungeeVariables.PACKET_BROADCAST_PACKET;
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
