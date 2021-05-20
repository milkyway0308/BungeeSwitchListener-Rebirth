package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer

class LongSerializer : IByteBufSerializer<Long> {
    override fun ByteBuf.writePacketHeader(data: Long) {
        writeLong(data)
    }

    override fun ByteBuf.writePacketField(data: Long) {
        // Do nothing on primitive
    }

    override fun ByteBuf.readPacketHeader() = readLong()

    override fun ByteBuf.readPacketField(orig: Long) {
        // Do nothing on primitive
    }
}