package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.DataMode

class ByteArraySerializer : IByteBufSerializer<ByteArray> {

    override fun ByteBuf.writePacketHeader(data: ByteArray) {
        writeInt(data.size)
        writeBytes(data)
    }

    override fun ByteBuf.writePacketField(data: ByteArray) {
        // Do nothing on primitive
    }

    override fun ByteBuf.readPacketHeader(): ByteArray {
        return ByteArray(readInt()).apply {
            readBytes(this)
        }
    }

    override fun ByteBuf.readPacketField(orig: ByteArray) {
        // Do nothing on primitive
    }

}