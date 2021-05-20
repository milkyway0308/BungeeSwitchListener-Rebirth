package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.DataMode
import skywolf46.bsl.core.util.readString
import skywolf46.bsl.core.util.writeString

class StringSerializer : IByteBufSerializer<String> {

    override fun ByteBuf.writePacketHeader(data: String) {
        writeString(data)
    }

    override fun ByteBuf.writePacketField(data: String) {
        // Do nothing on primitive
    }

    override fun ByteBuf.readPacketHeader(): String = readString()

    override fun ByteBuf.readPacketField(orig: String) {
        // Do nothing on primitive
    }

}

