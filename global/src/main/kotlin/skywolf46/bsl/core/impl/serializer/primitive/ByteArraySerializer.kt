package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.DataMode

class ByteArraySerializer : IByteBufSerializer<ByteArray> {
    override fun ByteBuf.writeBuffer(data: ByteArray, mode: DataMode) {
        writeInt(data.size)
        writeBytes(data)
        println("Written size: ${data.size}")
    }

    override fun ByteBuf.readBuffer(readMode: DataMode): ByteArray {
        val size = readInt()
        return ByteArray(size).apply {
            readBytes(this)
        }
    }

    override fun ByteBuf.readBuffer(orig: ByteArray, readMode: DataMode) {
        // Do nothing on primitive
    }

    override fun ByteBuf.writePacketHeader(data: ByteArray) {
        // Do nothing on primitive
    }
}