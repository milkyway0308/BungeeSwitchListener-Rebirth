package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.DataMode

class ByteSerializer : IByteBufSerializer<Byte> {

    override fun ByteBuf.writePacketHeader(data: Byte) {
        writeByte(data.toInt())
    }

    override fun ByteBuf.writePacketField(data: Byte) {
        // Do nothing on primitive
    }

    override fun ByteBuf.readPacketHeader(): Byte = readByte()

    override fun ByteBuf.readPacketField(orig: Byte) {
        // Do nothing on primitive
    }

}