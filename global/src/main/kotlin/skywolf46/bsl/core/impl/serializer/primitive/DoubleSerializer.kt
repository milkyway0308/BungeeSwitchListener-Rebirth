package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.DataMode

class DoubleSerializer : IByteBufSerializer<Double> {
    override fun ByteBuf.writeBuffer(data: Double, mode: DataMode) {
        writeDouble(data)
    }

    override fun ByteBuf.readBuffer(readMode: DataMode): Double {
        return readDouble()
    }

    override fun ByteBuf.readBuffer(orig: Double, readMode: DataMode) {
        // Do nothing on primitive
    }

    override fun ByteBuf.writePacketHeader(data: Double) {
        // Do nothing on primitive
    }
}