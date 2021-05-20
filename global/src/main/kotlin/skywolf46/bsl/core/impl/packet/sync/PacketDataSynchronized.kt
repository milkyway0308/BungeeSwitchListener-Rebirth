package skywolf46.bsl.core.impl.packet.sync

import io.netty.buffer.Unpooled
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.AbstractContainerPacketBase
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.abstraction.IBSLPacket
import skywolf46.bsl.core.annotations.BSLHeader
import skywolf46.bsl.core.impl.packet.proxy.PacketRequireProxy
import skywolf46.bsl.core.util.CoveredIntRange
import skywolf46.bsl.core.util.asLookUp
import skywolf46.bsl.core.util.writeIntRange

class PacketDataSynchronized(
    @BSLHeader var timestamp: Long,
    packetRange: IntRange,
    packetContainer: ByteArray,
) : AbstractContainerPacketBase<PacketRequireProxy>(packetRange, packetContainer) {
    constructor() : this(0L,  0..0, byteArrayOf())

    constructor(
        timestamp: Long,
        className: String, packet: AbstractPacketBase<*>,
    ) : this(timestamp,
        packet.javaClass.asLookUp().toRange(),
        BSLCore.resolve(packet::class.java as Class<Any>).let { serializer ->
            val buf = Unpooled.buffer()
            BSLCore.afterProcessor(packet.javaClass).beforeWrite.forEach {
                it.data.invoke(packet)
            }
            serializer.writeHeaderData(buf, packet)
            serializer.writeFieldData(buf, packet)
            return@let buf.array().apply {
                buf.release()
            }
        })

    fun isPacketOf(cls: Class<*>): Boolean {
        return CoveredIntRange(cls.asLookUp().toRange()) == CoveredIntRange(range)
    }


}