package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.ReadingMode

class ShortSerializer : IByteBufSerializer<Short> {
    override fun ByteBuf.writeBuffer(data: Short) {
        writeShort(data.toInt())
    }

    override fun ByteBuf.readBuffer(readMode: ReadingMode): Short {
        return readShort()
    }

    override fun ByteBuf.readBuffer(orig: Short, readMode: ReadingMode) {
        // Do nothing on primitive
    }
}