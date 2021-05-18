package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.DataMode

class ShortSerializer : IByteBufSerializer<Short> {
    override fun ByteBuf.writeBuffer(data: Short, mode: DataMode) {
        writeShort(data.toInt())
    }

    override fun ByteBuf.readBuffer(readMode: DataMode): Short {
        return readShort()
    }

    override fun ByteBuf.readBuffer(orig: Short, readMode: DataMode) {
        // Do nothing on primitive
    }

    override fun ByteBuf.writePacketHeader(data: Short) {
        // Do nothing on primitive
    }
}