package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.DataMode

class FloatSerializer : IByteBufSerializer<Float> {

    override fun ByteBuf.writePacketHeader(data: Float) {
        writeFloat(data)
    }

    override fun ByteBuf.writePacketField(data: Float) {
        // Do nothing on primitive
    }

    override fun ByteBuf.readPacketHeader(): Float = readFloat()

    override fun ByteBuf.readPacketField(orig: Float) {
        // Do nothing on primitive
    }

}