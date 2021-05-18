package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.DataMode

class IntSerializer : IByteBufSerializer<Int> {
    override fun ByteBuf.writeBuffer(data: Int, mode: DataMode) {
        writeInt(data)
    }

    override fun ByteBuf.readBuffer(readMode: DataMode): Int = readInt()

    override fun ByteBuf.readBuffer(orig: Int, readMode: DataMode) {
        // Do nothing on primitive
    }

    override fun ByteBuf.writePacketHeader(data: Int) {
        // Do nothing on primitive
    }
}