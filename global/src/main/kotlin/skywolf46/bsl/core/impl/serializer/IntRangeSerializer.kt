package skywolf46.bsl.core.impl.serializer

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.util.readIntRange
import skywolf46.bsl.core.util.writeIntRange

class IntRangeSerializer : IByteBufSerializer<IntRange>{
    override fun ByteBuf.writePacketHeader(data: IntRange) {
        writeIntRange(data)
    }

    override fun ByteBuf.writePacketField(data: IntRange) {
        // Do nothing on primitive
    }

    override fun ByteBuf.readPacketHeader(): IntRange {
        return readIntRange()
    }

    override fun ByteBuf.readPacketField(orig: IntRange) {
        // Do nothing on primitive
    }

}