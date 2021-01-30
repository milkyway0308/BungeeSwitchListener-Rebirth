package skywolf46.bsl.global.abstraction.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import skywolf46.bsl.global.abstraction.enums.Side;
import skywolf46.bsl.global.util.ByteBufWrapper;

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

    public <T extends AbstractPacket> void attachListener(Class<T> cls, BiConsumer<Channel, T> listen) {
        listener.add((BiConsumer<Channel, AbstractPacket>) listen);
    }

    public void register(PacketWrapperWriter<?> writer) {
        this.writer = writer;
    }

    public void register(PacketWrapperReader<?> reader) {
        this.reader = reader;
    }

    @Deprecated
    public void register(PacketWriter<?> writer) {
        this.writer = writer;
    }

    @Deprecated
    public void register(PacketReader<?> reader) {
        this.reader = reader;
    }

    public <T extends AbstractPacket> void register(Class<T> packet, PacketWriter<T> writer) {
        register(writer);
    }


    public <T extends AbstractPacket> void register(Class<T> packet, PacketReader<T> writer) {
        register(writer);
    }

    public PacketWriter<?> writer() {
        return writer;
    }

    public PacketReader<?> reader() {
        return reader;
    }

    public void listen(Channel channel, AbstractPacket packetForged) {
        packetForged.markBuffer();
        for (BiConsumer<Channel, AbstractPacket> cons : listener) {
            cons.accept(channel, packetForged);
            packetForged.resetBuffer();
        }
        packetForged.releaseBuffer();
    }

    public abstract int getID();

    public void retainBuffer() {

    }

    public void releaseBuffer() {

    }

    public void markBuffer() {

    }

    public void resetBuffer() {

    }

    @FunctionalInterface
    public interface PacketWriter<T extends AbstractPacket> {
        void write(T packet, ByteBuf buffer);
    }

    @FunctionalInterface
    public interface PacketReader<T extends AbstractPacket> {
        T read(ByteBuf buf);
    }

    public static abstract class PacketWrapperWriter<T extends AbstractPacket> implements PacketWriter<T> {
        @Override
        public void write(T packet, ByteBuf buffer) {
            write(packet, new ByteBufWrapper<>(buffer, packet));
        }

        public abstract void write(T Packet, ByteBufWrapper<T> wrapper);
    }

    public static abstract class PacketWrapperReader<T extends AbstractPacket> implements PacketReader<T> {
        @Override
        public T read(ByteBuf buf) {
            return read(new ByteBufWrapper<>(buf, null));
        }

        public abstract T read(ByteBufWrapper<Object> reader);
    }
}
