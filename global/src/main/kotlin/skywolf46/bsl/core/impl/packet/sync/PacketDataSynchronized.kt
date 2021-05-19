package skywolf46.bsl.core.impl.packet.sync

import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.annotations.BSLHeader

class PacketDataSynchronized(@BSLHeader var className: String, private var packetContainer: ByteArray) :
    AbstractPacketBase<PacketDataSynchronized>() {
    constructor() : this("", byteArrayOf())
    constructor(clazz: Class<*>, container: ByteArray) : this(clazz.name, container)

    fun isPacketOf(cls: Class<Any>): Boolean {
        return cls.name == className
    }
}