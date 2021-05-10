package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer

class FloatSerializer : IByteBufSerializer<Float> {
    override fun ByteBuf.writeBuffer(data: Float) {
        writeFloat(data)
    }

    override fun ByteBuf.readBuffer(onlyHeader: Boolean): Float = readFloat()
}