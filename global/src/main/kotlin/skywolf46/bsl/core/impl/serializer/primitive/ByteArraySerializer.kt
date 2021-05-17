package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.ReadingMode

class ByteArraySerializer : IByteBufSerializer<ByteArray> {
    override fun ByteBuf.writeBuffer(data: ByteArray) {
        writeInt(data.size)
        writeBytes(data)
    }

    override fun ByteBuf.readBuffer(readMode: ReadingMode): ByteArray {
        return ByteArray(readInt()).apply {
            readBytes(this)
        }
    }
}