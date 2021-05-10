package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer

class DoubleSerializer : IByteBufSerializer<Double> {
    override fun ByteBuf.writeBuffer(data: Double) {
        writeDouble(data)
    }

    override fun ByteBuf.readBuffer(onlyHeader: Boolean): Double {
        return readDouble()
    }
}