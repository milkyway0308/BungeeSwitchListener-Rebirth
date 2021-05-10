package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer

class IntSerializer : IByteBufSerializer<Int> {
    override fun ByteBuf.writeBuffer(data: Int) {
        writeInt(data)
    }

    override fun ByteBuf.readBuffer(onlyHeader: Boolean): Int = readInt()
}