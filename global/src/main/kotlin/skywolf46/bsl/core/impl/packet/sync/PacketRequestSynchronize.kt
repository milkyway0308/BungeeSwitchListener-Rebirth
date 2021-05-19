package skywolf46.bsl.core.impl.packet.sync

import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.annotations.BSLHeader

class PacketRequestSynchronize(@BSLHeader var className: String) : AbstractPacketBase<PacketRequestSynchronize>() {
    constructor() : this("")
    constructor(clazz: Class<*>) : this(clazz.name)

    fun isPacketOf(cls: Class<Any>): Boolean {
        return cls.name == className
    }
}