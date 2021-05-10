package skywolf46.bsl.core.data

import skywolf46.bsl.core.abstraction.AbstractPacketBase
import java.lang.IllegalStateException

data class PacketHeader<PACKET : AbstractPacketBase>(val fromPort: Int = -1, val responseFrom: String?, val data: PACKET) {

    fun response(packet: AbstractPacketBase) {
        if (fromPort == -1)
            throw IllegalStateException("Packet is not responsible")
        // TODO write packet to port
    }

    fun isResponseOf(cls: Class<out AbstractPacketBase>): Boolean {
        return responseFrom?.equals(cls.name) == true
    }
}