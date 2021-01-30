package skywolf46.bsl.global.impl.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import skywolf46.bsl.global.BungeeVariables;
import skywolf46.bsl.global.abstraction.enums.Side;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;

public class PacketReceivePayload extends AbstractPacket {
    private ByteBuf buffer;
    private int serverPort;

    public PacketReceivePayload(int port) {
        this(port, true);
    }

    public PacketReceivePayload(int port, boolean create) {
        this.serverPort = port;
        if (create)
            this.buffer = Unpooled.directBuffer();
    }

    public ByteBuf getBuffer() {
        return buffer;
    }

    public int getServerPort() {
        return serverPort;
    }

    @Override
    public int getID() {
        return BungeeVariables.PACKET_RECEIVE_PAYLOAD;
    }

    @Override
    public Side getSide() {
        return Side.SERVER;
    }

    @Override
    public void listen(Channel channel, AbstractPacket packetForged) {
        super.listen(channel, packetForged);
        buffer.release();
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
