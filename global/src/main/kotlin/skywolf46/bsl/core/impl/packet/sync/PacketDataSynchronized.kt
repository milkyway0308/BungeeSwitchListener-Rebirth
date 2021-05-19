package skywolf46.bsl.core.impl.packet.sync

import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.annotations.BSLHeader

class PacketDataSynchronized(
    @BSLHeader var timestamp: Long,
    var className: String,
    private var packetContainer: ByteArray,
) :
    AbstractPacketBase<PacketDataSynchronized>() {
    constructor() : this(0L, "", byteArrayOf())
    constructor(timestamp: Long, clazz: Class<*>, container: ByteArray) : this(timestamp, clazz.name, container)

    fun isPacketOf(cls: Class<Any>): Boolean {
        return cls.name == className
    }

}