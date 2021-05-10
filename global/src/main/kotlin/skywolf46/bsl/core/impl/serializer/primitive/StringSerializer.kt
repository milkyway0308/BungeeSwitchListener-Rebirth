package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer

class StringSerializer : IByteBufSerializer<String> {
    override fun ByteBuf.writeBuffer(data: String) {
        writeInt(data.length)
        writeBytes(data.toByteArray())
    }

    override fun ByteBuf.readBuffer(onlyHeader: Boolean): String {
        with(ByteArray(readInt())) {
            readBytes(this)
            return String(this)
        }
    }
}