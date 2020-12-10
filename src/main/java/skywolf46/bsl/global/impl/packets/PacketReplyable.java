package skywolf46.bsl.global.impl.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import skywolf46.bsl.global.BungeeVariables;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;
import skywolf46.bsl.global.api.BSLCoreAPI;

import java.util.ArrayList;
import java.util.List;

public class PacketReplyable extends AbstractPacket {
    private int port;
    private ByteBuf targetPacket;
    private int packetAmounts = 0;

    public PacketReplyable() {

    }

    public PacketReplyable(int port) {
        targetPacket = Unpooled.directBuffer();
        targetPacket.writeInt(0);
        targetPacket.writeInt(port);
    }

    public PacketReplyable(ByteBuf target) {
        this.targetPacket = target;
        this.packetAmounts = target.readInt();
        this.port = target.readInt();
    }

    public void addPacket(AbstractPacket packet) {
        ++packetAmounts;
        packet.writer().write(packet, targetPacket);
    }

    public ByteBuf finalizeBuffer() {
        int position = targetPacket.writerIndex();
        targetPacket.writerIndex(0);
        targetPacket.writeInt(packetAmounts);
        targetPacket.writerIndex(position);
        return targetPacket;
    }

    private ByteBuf getBuffer(){
        return targetPacket;
    }


    public List<AbstractPacket> restore() {
        List<AbstractPacket> packets = new ArrayList<>();
        int amount = targetPacket.readInt();
        for (int i = 0; i < amount; i++) {
            AbstractPacket pac = BSLCoreAPI.getPacket(targetPacket.readInt());
            pac = pac.reader().read(targetPacket);
            packets.add(pac);
        }
        return packets;
    }

    public void reply(AbstractPacket packet) {
        BSLCoreAPI.server(port).send(packet);
    }

    @Override
    public int getID() {
        return BungeeVariables.PACKET_REPLY_WRAPPER;
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
