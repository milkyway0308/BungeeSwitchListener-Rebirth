package skywolf46.bsl.core.abstraction

import io.netty.buffer.Unpooled
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.annotations.BSLHeader

abstract class AbstractContainerPacketBase<X : AbstractPacketBase<X>>(
    @BSLHeader var range: IntRange,
    @BSLHeader var packet: ByteArray,
) :
    AbstractPacketBase<X>() {

    fun unwrap(): IBSLPacket {
        val buf = Unpooled.wrappedBuffer(packet)
        val packet = BSLCore.classLookup.lookUpRangeValue(range)?.readFully(buf)
            ?: throw IllegalStateException("Cannot deserialize packet range in ${range.first}..${range.last}")
        buf.release()
        return packet as IBSLPacket
    }

}