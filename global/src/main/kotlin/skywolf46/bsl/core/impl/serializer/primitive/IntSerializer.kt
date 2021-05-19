package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.DataMode

class IntSerializer : IByteBufSerializer<Int> {

    override fun ByteBuf.writePacketHeader(data: Int) {
        writeInt(data)
    }

    override fun ByteBuf.writePacketField(data: Int) {
        // Do nothing on primitive
    }

    override fun ByteBuf.readPacketHeader(): Int = readInt()

    override fun ByteBuf.readPacketField(orig: Int) {
        // Do nothing on primitive
    }
}