package skywolf46.bsl.core.impl.packet.sync

import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.abstraction.AbstractPacketSyncRequest
import skywolf46.bsl.core.annotations.BSLHeader
import skywolf46.bsl.core.util.CoveredIntRange
import skywolf46.bsl.core.util.asLookUp
import java.util.*

class PacketCannotSynchronize(var timestamp: Long, @BSLHeader var uuid: UUID, @BSLHeader var classRange: IntRange) :
    AbstractPacketBase<PacketCannotSynchronize>() {
    constructor() : this(0L, UUID.randomUUID(), 0..0)
    constructor(timestamp: Long, serverUUID: UUID, clazz: Class<out AbstractPacketSyncRequest<*>>) : this(timestamp,
        serverUUID, clazz.asLookUp().toRange())

    fun isPacketOf(cls: Class<out AbstractPacketSyncRequest<*>>): Boolean {
        return CoveredIntRange(cls.asLookUp().toRange()) == CoveredIntRange(classRange)
    }

}