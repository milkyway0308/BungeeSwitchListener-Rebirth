package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer

class BooleanSerializer : IByteBufSerializer<Boolean> {
    override fun ByteBuf.writeBuffer(data: Boolean) {
        writeBoolean(data)
    }

    override fun ByteBuf.readBuffer(onlyHeader: Boolean): Boolean = readBoolean()
}