package skywolf46.bsl.core.impl.packet.sync

import io.netty.buffer.Unpooled
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.annotations.BSLHeader
import skywolf46.bsl.core.enums.DataMode
import skywolf46.bsl.core.security.permissions.SecurityPermissions
import skywolf46.bsl.core.util.asLookUp
import java.lang.IllegalStateException

class PacketRequestSynchronize(@BSLHeader private var timestamp: Long, var className: String) :
    AbstractPacketBase<PacketRequestSynchronize>() {
    @BSLHeader
    var isResponded = false
        private set

    constructor() : this(0L, "")
    constructor(timestamp: Long, clazz: Class<*>) : this(timestamp, clazz.name)

    override fun requirePermission(): SecurityPermissions {
        return SecurityPermissions.ADMIN
    }

    fun isPacketOf(cls: Class<*>): Boolean {
        return cls.name == className
    }

    fun deny() {
        if (isResponded)
            throw IllegalStateException("Already responded")
        isResponded = true
        header.response(PacketCannotSynchronize(timestamp, className))
    }

    fun accept(packet: AbstractPacketBase<*>) {
        if (isResponded)
            throw IllegalStateException("Already responded")
        isResponded = true
        header.response(PacketDataSynchronized(timestamp, className, packet.javaClass.asLookUp().toRange(), packet.run {
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