package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.DataMode

class FloatSerializer : IByteBufSerializer<Float> {
    override fun ByteBuf.writeBuffer(data: Float, mode: DataMode) {
        writeFloat(data)
    }

    override fun ByteBuf.readBuffer(readMode: DataMode): Float = readFloat()

    override fun ByteBuf.readBuffer(orig: Float, readMode: DataMode) {
        // Do nothing on primitive
    }

    override fun ByteBuf.writePacketHeader(data: Float) {
        // Do nothing on primitive
    }
}