package skywolf46.bsl.global.util;

import io.netty.buffer.ByteBuf;

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

    public ByteBufWrapper<T> writeBytes(ByteBufWrapper wp) {
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

    public byte readByte(){
        return buf.readByte();
    }

    public short readShort(){
        return buf.readShort();
    }

    public int readInt(){
        return buf.readInt();
    }

    public long readLong(){
        return buf.readLong();
    }

    public float readFloat(){
        return buf.readFloat();
    }

    public double readDouble(){
        return buf.readDouble();
    }

    public String readString(){
        return ByteBufUtility.readString(buf);
    }



}
