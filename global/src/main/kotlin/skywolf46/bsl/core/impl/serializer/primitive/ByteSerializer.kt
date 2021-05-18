package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.DataMode

class ByteSerializer : IByteBufSerializer<Byte> {
    override fun ByteBuf.writeBuffer(data: Byte, mode: DataMode) {
        writeByte(data.toInt())
    }

    override fun ByteBuf.readBuffer(readMode: DataMode): Byte = readByte()

    override fun ByteBuf.readBuffer(orig: Byte, readMode: DataMode) {
        // Do nothing on primitive
    }

    override fun ByteBuf.writePacketHeader(data: Byte) {
        // Do nothing on primitive
    }

}