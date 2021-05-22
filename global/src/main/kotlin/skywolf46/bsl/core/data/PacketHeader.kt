package skywolf46.bsl.core.data

import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.abstraction.IBSLServer
import skywolf46.bsl.core.annotations.BSLExclude
import skywolf46.bsl.core.impl.packet.PacketReplied

class PacketHeader<PACKET : AbstractPacketBase<*>> {
    @BSLExclude
    lateinit var server: IBSLServer
        internal set
    var targetName: String = "_BSLCore"

    fun response(packet: AbstractPacketBase<*>) {
        if (BSLCore.isServer) {
            packet.header.targetName = "_BSLCore"
            server.send(packet)
        } else {
            if (packet.header.targetName == "_BSLCore") {
                server.send(packet)
            } else {
                server.send(PacketReplied.of(packet.header.targetName, packet))
            }
        }
    }

}