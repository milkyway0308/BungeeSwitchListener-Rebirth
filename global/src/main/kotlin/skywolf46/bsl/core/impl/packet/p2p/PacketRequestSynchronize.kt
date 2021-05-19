package skywolf46.bsl.core.impl.packet.p2p

import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.annotations.BSLHandler
import skywolf46.bsl.core.annotations.BSLHeader

class PacketRequestSynchronize(@BSLHeader val className: String) : AbstractPacketBase<PacketRequestSynchronize>() {
    constructor() : this("")
    constructor(clazz: Class<*>) : this(clazz.name)


}