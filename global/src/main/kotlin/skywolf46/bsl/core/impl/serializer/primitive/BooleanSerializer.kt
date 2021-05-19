package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.DataMode

class BooleanSerializer : IByteBufSerializer<Boolean> {

    override fun ByteBuf.readPacketHeader(): Boolean = readBoolean()

    override fun ByteBuf.readPacketField(orig: Boolean) {
        // Do nothing on primitive
    }

    override fun ByteBuf.writePacketHeader(data: Boolean) {
        writeBoolean(data)
    }

    override fun ByteBuf.writePacketField(data: Boolean) {
        // Do nothing on primitive
    }

}