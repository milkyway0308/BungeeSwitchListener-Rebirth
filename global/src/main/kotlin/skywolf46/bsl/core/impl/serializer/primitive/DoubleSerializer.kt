package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.ReadingMode

class DoubleSerializer : IByteBufSerializer<Double> {
    override fun ByteBuf.writeBuffer(data: Double) {
        writeDouble(data)
    }

    override fun ByteBuf.readBuffer(readMode: ReadingMode): Double {
        return readDouble()
    }
}