package skywolf46.bsl.core.impl.packet.sync

import io.netty.buffer.Unpooled
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.AbstractContainerPacketBase
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.annotations.BSLHeader
import skywolf46.bsl.core.enums.DataMode
import skywolf46.bsl.core.security.permissions.SecurityPermissions
import skywolf46.bsl.core.util.CoveredIntRange
import skywolf46.bsl.core.util.asLookUp
import java.lang.IllegalStateException

class PacketRequestSynchronize(
    @BSLHeader private var timestamp: Long,
    range: IntRange,
    packetContainer: ByteArray,
) :
    AbstractContainerPacketBase<PacketRequestSynchronize>(range, packetContainer) {
    @BSLHeader
    var isResponded = false
        private set

    constructor() : this(0L, 0..0, byteArrayOf())

    constructor(timestamp: Long, packet: AbstractPacketBase<*>) : this(timestamp,
        packet.asLookUp().toRange(),
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

    override fun requirePermission(): SecurityPermissions {
        return SecurityPermissions.ADMIN
    }

    fun isPacketOf(cls: Class<*>): Boolean {
        return CoveredIntRange(cls.asLookUp().toRange()) == CoveredIntRange(range)
    }

    fun deny() {
        if (isResponded)
            throw IllegalStateException("Already responded")
        isResponded = true
        header.response(PacketCannotSynchronize(timestamp, range))
    }

    fun accept(packet: AbstractPacketBase<*>) {
        if (isResponded)
            throw IllegalStateException("Already responded")
        isResponded = true
        header.response(PacketDataSynchronized(timestamp,
            packet.javaClass.asLookUp().toRange(),
            packet.run {
                val buf = Unpooled.buffer()
                val writer = BSLCore.resolve(javaClass)
                writer.writeHeaderData(buf, packet)
                writer.writeFieldData(buf, packet)
                return@run buf.array().apply {
                    buf.release()
                }
            }))
    }

}