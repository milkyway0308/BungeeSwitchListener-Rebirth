package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.DataMode

class StringSerializer : IByteBufSerializer<String> {

    override fun ByteBuf.writePacketHeader(data: String) {
        val arr = data.toByteArray()
        writeInt(arr.size)
        writeBytes(arr)
    }

    override fun ByteBuf.writePacketField(data: String) {
        // Do nothing on primitive
    }

    override fun ByteBuf.readPacketHeader(): String {
        with(ByteArray(readInt())) {
            readBytes(this)
            return String(this)
        }
    }

    override fun ByteBuf.readPacketField(orig: String) {
        // Do nothing on primitive
    }

}

