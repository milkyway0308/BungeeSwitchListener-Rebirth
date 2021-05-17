package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.ReadingMode

class IntSerializer : IByteBufSerializer<Int> {
    override fun ByteBuf.writeBuffer(data: Int) {
        writeInt(data)
    }

    override fun ByteBuf.readBuffer(readMode: ReadingMode): Int = readInt()

    override fun ByteBuf.readBuffer(orig: Int, readMode: ReadingMode) {
        // Do nothing on primitive
    }
}