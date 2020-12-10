package skywolf46.bsl.global.impl.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import skywolf46.bsl.global.BungeeVariables;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;
import skywolf46.bsl.global.api.BSLCoreAPI;

public class PacketRelay extends AbstractPacket {
    private int port;
    private ByteBuf buffer;

    public PacketRelay(int port, AbstractPacket packet) {
        this.port = port;
        buffer = Unpooled.directBuffer();
        buffer.writeInt(port);
        buffer.writeInt(packet.getID());
        BSLCoreAPI.getPacket(packet.getID()).writer().write(packet, buffer);
    }

    public PacketRelay(ByteBuf buf) {
        this.port = buf.readInt();
        this.buffer = buf;
    }

    public PacketRelay(){

    }

    public ByteBuf getBuffer() {
        return buffer;
    }

    public int getPort() {
        return port;
    }

    @Override
    public int getID() {
        return BungeeVariables.PACKET_RELAY;
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
