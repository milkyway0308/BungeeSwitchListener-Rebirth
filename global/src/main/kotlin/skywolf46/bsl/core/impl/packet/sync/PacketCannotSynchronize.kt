package skywolf46.bsl.core.impl.packet.sync

import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.annotations.BSLHeader

class PacketCannotSynchronize(var timestamp: Long, @BSLHeader var className: String) :
    AbstractPacketBase<PacketCannotSynchronize>() {
    constructor() : this(0L,"")
    constructor(timestamp: Long, clazz: Class<*>) : this(timestamp, clazz.name)

    fun isPacketOf(cls: Class<*>): Boolean {
        return cls.name == className
    }
}