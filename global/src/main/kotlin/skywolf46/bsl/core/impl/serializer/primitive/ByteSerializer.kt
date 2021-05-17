package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.ReadingMode

class ByteSerializer : IByteBufSerializer<Byte> {
    override fun ByteBuf.writeBuffer(data: Byte) {
        writeByte(data.toInt())
    }

    override fun ByteBuf.readBuffer(readMode: ReadingMode): Byte = readByte()

}