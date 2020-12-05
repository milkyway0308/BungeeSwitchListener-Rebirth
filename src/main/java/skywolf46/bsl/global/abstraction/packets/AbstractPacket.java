package skywolf46.bsl.global.abstraction.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import skywolf46.bsl.global.abstraction.enums.Side;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class AbstractPacket {
    private PacketWriter writer;
    private PacketReader reader;
    private List<BiConsumer<Channel, AbstractPacket>> listener = new ArrayList<>();

    public Side getSide() {
        return Side.GLOBAL;
    }

    public void attachListener(BiConsumer<Channel, AbstractPacket> listen) {
        listener.add(listen);
    }


    public void register(PacketWriter writer) {
        this.writer = writer;
    }


    public void register(PacketReader reader) {
        this.reader = reader;
    }

    public PacketWriter writer() {
        return writer;
    }

    public PacketReader reader() {
        return reader;
    }

    public void listen(Channel channel, AbstractPacket packetForged) {
        for (BiConsumer<Channel, AbstractPacket> cons : listener)
            cons.accept(channel, packetForged);
    }

    public abstract int getID();

    @FunctionalInterface
    public interface PacketWriter<T extends AbstractPacket> {
        void write(T packet, ByteBuf buffer);
    }

    @FunctionalInterface
    public interface PacketReader<T extends AbstractPacket> {
        T read(ByteBuf buf);
    }
}
