package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.DataMode

class StringSerializer : IByteBufSerializer<String> {
    override fun ByteBuf.writeBuffer(data: String, mode: DataMode) {
        writeInt(data.length)
        writeBytes(data.toByteArray())
    }

    override fun ByteBuf.readBuffer(readMode: DataMode): String {
        with(ByteArray(readInt())) {
            readBytes(this)
            return String(this)
        }
    }

    override fun ByteBuf.readBuffer(orig: String, readMode: DataMode) {
        // Do nothing on primitive
    }

    override fun ByteBuf.writePacketHeader(data: String) {
        // Do nothing on primitive
    }
}