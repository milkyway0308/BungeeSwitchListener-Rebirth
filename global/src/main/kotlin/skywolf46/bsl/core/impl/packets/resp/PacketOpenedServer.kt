package skywolf46.bsl.core.impl.packets.resp

import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.annotations.annotations.BSLHeader
import skywolf46.bsl.core.data.PacketHeader

open class PacketOpenedServer<SELF : AbstractPacketBase> : AbstractPacketBase() {
    @BSLHeader
    var header: PacketHeader<SELF>? = null
        internal set

}