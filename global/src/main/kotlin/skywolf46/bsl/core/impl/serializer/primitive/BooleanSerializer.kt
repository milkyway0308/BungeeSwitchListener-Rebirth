package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.ReadingMode

class BooleanSerializer : IByteBufSerializer<Boolean> {
    override fun ByteBuf.writeBuffer(data: Boolean) {
        writeBoolean(data)
    }

    override fun ByteBuf.readBuffer(readMode: ReadingMode): Boolean = readBoolean()

    override fun ByteBuf.readBuffer(orig: Boolean, readMode: ReadingMode) {
        // Do nothing on primitive
    }
}