package skywolf46.bsl.global.impl.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import skywolf46.bsl.global.BungeeVariables;
import skywolf46.bsl.global.abstraction.enums.Side;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;

public class PacketTransferPayload extends AbstractPacket {
    private ByteBuf buffer;
    private int serverPort;

    public PacketTransferPayload(int port) {
        this(port, true);
    }

    public PacketTransferPayload(int port, boolean create) {
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
        return BungeeVariables.PACKET_TRANSFER_PAYLOAD;
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public void listen(Channel channel, AbstractPacket packetForged) {
        super.listen(channel, packetForged);
        buffer.release();
    }
}
