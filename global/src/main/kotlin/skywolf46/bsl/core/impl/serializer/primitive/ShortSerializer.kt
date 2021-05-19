package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.DataMode

class ShortSerializer : IByteBufSerializer<Short> {

    override fun ByteBuf.writePacketHeader(data: Short) {
        writeShort(data.toInt())
    }

    override fun ByteBuf.writePacketField(data: Short) {
        // Do nothing on primitive
    }

    override fun ByteBuf.readPacketHeader(): Short {
        return readShort()
    }

    override fun ByteBuf.readPacketField(orig: Short) {
        // Do nothing on primitive
    }

}