package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.DataMode

class BooleanSerializer : IByteBufSerializer<Boolean> {
    override fun ByteBuf.writeBuffer(data: Boolean, mode: DataMode) {
        writeBoolean(data)
    }

    override fun ByteBuf.readBuffer(readMode: DataMode): Boolean = readBoolean()

    override fun ByteBuf.readBuffer(orig: Boolean, readMode: DataMode) {
        // Do nothing on primitive
    }

    override fun ByteBuf.writePacketHeader(data: Boolean) {
        // Do nothing on primitive
    }
}