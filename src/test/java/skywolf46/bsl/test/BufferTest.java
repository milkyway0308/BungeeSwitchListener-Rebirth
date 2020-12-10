package skywolf46.bsl.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class BufferTest {
    public static void main(String[] args) {
        ByteBuf bf = Unpooled.directBuffer();
        bf.writeInt(1);
        bf.writeInt(2);
        bf.writeInt(3);
        bf.writeInt(6);
        bf.writeInt(4);
        bf.writeInt(1);
        int ndx = bf.writerIndex();
        bf.writerIndex(0);
        bf.writeInt(941);
        bf.writerIndex(ndx);
        System.out.println(bf.readInt());
        System.out.println(bf.readInt());
        System.out.println(bf.readInt());
    }
}
