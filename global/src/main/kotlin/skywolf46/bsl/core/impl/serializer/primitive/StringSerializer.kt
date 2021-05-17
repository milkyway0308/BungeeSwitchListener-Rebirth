package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.ReadingMode

class StringSerializer : IByteBufSerializer<String> {
    override fun ByteBuf.writeBuffer(data: String) {
        writeInt(data.length)
        writeBytes(data.toByteArray())
    }

    override fun ByteBuf.readBuffer(readMode: ReadingMode): String {
        with(ByteArray(readInt())) {
            readBytes(this)
            return String(this)
        }
    }

    override fun ByteBuf.readBuffer(orig: String, readMode: ReadingMode) {
        // Do nothing on primitive
    }
}