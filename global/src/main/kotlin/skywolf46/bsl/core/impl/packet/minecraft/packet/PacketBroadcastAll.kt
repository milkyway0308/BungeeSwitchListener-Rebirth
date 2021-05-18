package skywolf46.bsl.core.impl.packet.minecraft.packet

import skywolf46.bsl.core.abstraction.AbstractPacketBase

class PacketBroadcastAll : AbstractPacketBase<PacketBroadcastAll>() {
    companion object {
        fun of(vararg msg: String): PacketBroadcastAll {
            val packet = PacketBroadcastAll()
            packet.lst.addAll(msg.toList())
            return packet
        }
    }

    val lst = mutableListOf<String>()
}