package skywolf46.bsl.global.util;

import io.netty.buffer.ByteBuf;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ByteBufWrapper<T> {
    private ByteBuf buf;
    private T original;

    public ByteBufWrapper(ByteBuf buf, T orig) {
        this.buf = buf;
        this.original = orig;
    }

    public T getOriginal() {
        return original;
    }

    public ByteBufWrapper<T> writeBytes(byte[] bx) {
        buf.writeBytes(bx);
        return this;
    }

    public ByteBufWrapper<T> writeBytes(ByteBuf buf) {
        this.buf.writeBytes(buf);
        return this;
    }

    public ByteBufWrapper<T> writeBytes(ByteBufWrapper<?> wp) {
        this.buf.writeBytes(wp.buf);
        return this;
    }

    public ByteBufWrapper<T> writeBoolean(boolean x) {
        buf.writeBoolean(x);
        return this;
    }

    public ByteBufWrapper<T> writeByte(byte x) {
        buf.writeByte(x);
        return this;
    }


    public ByteBufWrapper<T> writeShort(short x) {
        buf.writeShort(x);
        return this;
    }


    public ByteBufWrapper<T> writeInt(int x) {
        buf.writeInt(x);
        return this;
    }

    public ByteBufWrapper<T> writeLong(long x) {
        buf.writeLong(x);
        return this;
    }


    public ByteBufWrapper<T> writeFloat(float x) {
        buf.writeFloat(x);
        return this;
    }


    public ByteBufWrapper<T> writeDouble(double x) {
        buf.writeDouble(x);
        return this;
    }

    public ByteBufWrapper<T> writeString(String x) {
        ByteBufUtility.writeString(buf, x);
        return this;
    }

    public ByteBufWrapper<T> writeByteArray(byte[] bx) {
        writeInt(bx.length);
        writeBytes(bx);
        return this;
    }

    public <X> ByteBufWrapper<T> writeList(List<X> lst, BiConsumer<ByteBufWrapper<T>, X> writer) {
        writeInt(lst.size());
        for (X x : lst)
            writer.accept(this, x);
        return this;
    }

    public ByteBufWrapper<T> writeStringList(List<String> lx) {
        return writeList(lx, ByteBufWrapper::writeString);
    }

    public ByteBufWrapper<T> writeIntegerList(List<Integer> lx) {
        return writeList(lx, ByteBufWrapper::writeInt);
    }

    public ByteBufWrapper<T> writeDoubleList(List<Double> lx) {
        return writeList(lx, ByteBufWrapper::writeDouble);
    }

    public <XKey, XValue> ByteBufWrapper<T> writeMap(Map<XKey, XValue> map, BiConsumer<ByteBufWrapper<T>, XKey> keyWriter, BiConsumer<ByteBufWrapper<T>, XValue> valueWriter) {
        writeInt(map.size());
        for (Map.Entry<XKey, XValue> xv : map.entrySet()) {
            keyWriter.accept(this, xv.getKey());
            valueWriter.accept(this, xv.getValue());
        }
        return this;
    }


    public <XKey, XValue> ByteBufWrapper<T> writeMap(Class<XKey> keyClass, Class<XValue> valueClass, Map<XKey, XValue> map, BiConsumer<ByteBufWrapper<T>, XKey> keyWriter, BiConsumer<ByteBufWrapper<T>, XValue> valueWriter) {
        return writeMap(map, keyWriter, valueWriter);
    }

    public <XValue> ByteBufWrapper<T> writeStringKeyMap(Map<String, XValue> map, BiConsumer<ByteBufWrapper<T>, XValue> writer) {
        writeMap(map, (buf, key) -> writeString(key), writer);
        return this;
    }


    public <XValue> ByteBufWrapper<T> writeStringKeyMap(Class<XValue> valueClass, Map<String, XValue> map, BiConsumer<ByteBufWrapper<T>, XValue> writer) {
        return writeStringKeyMap(map, writer);
    }


    public ByteBufWrapper<T> writeUUID(UUID uid) {
        ByteBufUtility.writeUUID(buf, uid);
        return this;
    }

    public <OPT> ByteBufWrapper<T> writeOptional(OPT opt, BiConsumer<ByteBufWrapper<?>, OPT> cons) {
        if (opt == null) {
            writeBoolean(false);
        } else {
            writeBoolean(true);
            cons.accept(this, opt);
        }
        return this;
    }

    public boolean readBoolean() {
        return buf.readBoolean();
    }

    public byte readByte() {
        return buf.readByte();
    }

    public short readShort() {
        return buf.readShort();
    }

    public int readInt() {
        return buf.readInt();
    }

    public long readLong() {
        return buf.readLong();
    }

    public float readFloat() {
        return buf.readFloat();
    }

    public double readDouble() {
        return buf.readDouble();
    }

    public String readString() {
        return ByteBufUtility.readString(buf);
    }

    public <OPT> Optional<OPT> readOptional(Function<ByteBufWrapper<?>, OPT> funct) {
        if (readBoolean())
            return Optional.of(funct.apply(this));
        return Optional.empty();
    }

    public byte[] readByteArray() {
        byte[] bx = new byte[readInt()];
        buf.readBytes(bx);
        return bx;

    }

    public <XT> List<XT> readList(Function<ByteBufWrapper<?>, XT> reader) {
        List<XT> lt = new ArrayList<>();
        int length = readInt();
        for (int i = 0; i < length; i++)
            lt.add(reader.apply(this));
        return lt;
    }

    public <XKey, XValue> Map<XKey, XValue> readMap(Function<ByteBufWrapper<?>, XKey> keyReader, Function<ByteBufWrapper<?>, XValue> valueReader) {
        HashMap<XKey, XValue> map = new HashMap<>();
        int len = readInt();
        for (int i = 0; i < len; i++) {
            map.put(keyReader.apply(this), valueReader.apply(this));
        }
        return map;
    }

    public <XValue> Map<String, XValue> readStringKeyMap(Function<ByteBufWrapper<?>, XValue> valueReader) {
        return readMap(ByteBufWrapper::readString, valueReader);
    }


    public <XValue> Map<Integer, XValue> readIntKeyMap(Function<ByteBufWrapper<?>, XValue> valueReader) {
        return readMap(ByteBufWrapper::readInt, valueReader);
    }


    public <XValue> Map<Long, XValue> readLongMap(Function<ByteBufWrapper<?>, XValue> valueReader) {
        return readMap(ByteBufWrapper::readLong, valueReader);
    }


    public UUID readUUID() {
        return ByteBufUtility.readUUID(buf);
    }

}
