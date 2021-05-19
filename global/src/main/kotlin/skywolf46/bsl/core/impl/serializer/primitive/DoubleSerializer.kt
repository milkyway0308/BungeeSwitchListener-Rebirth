package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.DataMode

class DoubleSerializer : IByteBufSerializer<Double> {

    override fun ByteBuf.writePacketHeader(data: Double) {
        writeDouble(data)
    }

    override fun ByteBuf.writePacketField(data: Double) {
        // Do nothing on primitive
    }

    override fun ByteBuf.readPacketHeader(): Double {
        return readDouble()
    }

    override fun ByteBuf.readPacketField(orig: Double) {
        // Do nothing on primitive
    }

}