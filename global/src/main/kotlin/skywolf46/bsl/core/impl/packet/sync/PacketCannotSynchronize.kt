package skywolf46.bsl.core.impl.packet.sync

import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.annotations.BSLHeader
import skywolf46.bsl.core.util.CoveredIntRange
import skywolf46.bsl.core.util.asLookUp

class PacketCannotSynchronize(var timestamp: Long, @BSLHeader var classRange: IntRange) :
    AbstractPacketBase<PacketCannotSynchronize>() {
    constructor() : this(0L, 0..0)
    constructor(timestamp: Long, clazz: Class<*>) : this(timestamp, clazz.asLookUp().toRange())

    fun isPacketOf(cls: Class<*>): Boolean {
        return CoveredIntRange(cls.asLookUp().toRange()) == CoveredIntRange(classRange)
    }

}